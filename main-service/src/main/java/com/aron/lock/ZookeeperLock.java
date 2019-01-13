package com.aron.lock;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description:
 * <p>re-entrant DistributedLock .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/1/13        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/1/13 03:05
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Slf4j
@Component
public class ZookeeperLock implements InitializingBean, CimLock {

    private final String lockRootPath;

    private CuratorFramework zkClient;

    /**
     * 锁等待时间，防止线程饥饿
     */
    private int timeoutMsecs = 6 * 1000;

    private final static ThreadLocal<LockData> CURRENT_LOCK = new ThreadLocal<>();

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    @Autowired
    public ZookeeperLock(@Qualifier("lockRootPath") String lockRootPath, CuratorFramework zkClient) {
        log.info("lockRootPath is {}", lockRootPath);
        this.lockRootPath = lockRootPath;
        this.zkClient = zkClient;
    }

    private String setPath(Object lockKey) {
        Assert.isInstanceOf(String.class, lockKey);
        String format = String.format("%s/%s", lockRootPath, lockKey);
        setCurrentLock(format);
        return format;
    }

    /**
     * 获取分布式锁
     */
    @Override
    public boolean tryLock(Object lockKey, long waitingTime) {
        String path = setPath(lockKey);
        int timeout = timeoutMsecs;
        while (timeout > 0) {
            try {
                Assert.notNull(getCurrentLock(), "please obtain lock before tryLock");
                if (Boolean.TRUE.equals(getCurrentLock().getAllPathsInThread().get(path))) {
                    //same thread can re-obtain this lock
                    log.info("thread: {} get same lock");
                    return true;
                }
                createPath(path);
                return true;
            } catch (Exception e) {
                log.info("waiting to retry .......");
                try {
                    if (countDownLatch.getCount() <= 0) {
                        countDownLatch = new CountDownLatch(1);
                    }
                    timeout -= waitingTime;
                    countDownLatch.await(waitingTime, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ie) {
                    log.error("occurred error when acquire lock", ie);
                }
            }
        }
        log.error("!!! try lock timed out !!!");
        return false;
    }

    @Override
    public boolean tryLock(Object lockKey) {
        return tryLock(lockKey, 100);
    }

    @Override
    public boolean tryLockNoWaiting(Object lockKey) {
        String path = setPath(lockKey);
        try {
            Assert.notNull(getCurrentLock(), "please obtain lock before tryLock");
            createPath(path);
            return true;
        } catch (Exception e) {
            log.info("failed to acquire lock for path:{}", getCurrentLock());
            return false;
        }
    }

    private void createPath(String path) throws Exception {
        Assert.hasLength(path, "path can not be empty");
        zkClient.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(path);
        log.info("success to acquire lock for path:{}", path);
        //set locked flag to true
        getCurrentLock().getAllPathsInThread().put(path, true);
    }

    /**
     * 释放分布式锁
     */
    @Override
    public void unlock() {
        int newLockCount = 0;
        try {
            AtomicInteger lockCount = getCurrentLock().getLockCount();
            newLockCount = lockCount.decrementAndGet();
            if (newLockCount > 0) {
                log.info("lock count not zero");
                return;
            }
            if (newLockCount < 0) {
                throw new IllegalMonitorStateException("Lock count has gone negative for lock");
            }
            deletePathInThread();
        } catch (Exception e) {
            log.error("failed to release lock", e);
        } finally {
            if (newLockCount <= 0) {
                clearCurrentLock();
            }
        }
    }

    @Override
    public void unlockAll() {
        try {
            deletePathInThread();
        } catch (Exception e) {
            log.error("failed to release lock", e);
        } finally {
            clearCurrentLock();
        }
    }

    private void deletePathInThread() {
        Map<String, Boolean> allPathsInThread = getCurrentLock().getAllPathsInThread();
        allPathsInThread.forEach((path, locked) -> {
            if (Boolean.TRUE.equals(locked)) {
                try {
                    if (zkClient.checkExists().forPath(path) != null) {
                        zkClient.delete().forPath(path);
                        log.info("deleted path {}", path);
                    }
                } catch (Exception e) {
                    log.error("failed to delete path", e);
                }
            }
        });
    }

    /**
     * addWatcher
     *
     * @param lockPath lockPath
     */
    private void addWatcher(String lockPath) {
        PathChildrenCache watcher = new PathChildrenCache(zkClient, lockPath, false);
        try {
            watcher.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            log.info("watcher started");
            watcher.getListenable().addListener((client1, event) -> {
                ChildData data = event.getData();
                PathChildrenCacheEvent.Type type = event.getType();
                if (type.equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    String oldPath = data.getPath();
                    log.info("!!!!!!!!!!!!!!!!! success to release lock for path:{}", oldPath);
                    if (oldPath.contains(lockPath)) {
                        //释放计数器，让当前的请求获取锁
                        countDownLatch.countDown();
                    }
                }
            });
        } catch (Exception e) {
            log.error("watcher start failed", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        zkClient = zkClient.usingNamespace("lock-namespace");
        try {
            if (zkClient.checkExists().forPath(lockRootPath) == null) {
                zkClient.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(lockRootPath);
            }
            addWatcher(lockRootPath);
            log.info("create root path watcher success");
        } catch (Exception e) {
            log.error("connect zookeeper fail，please check the log >> {}", e.getMessage(), e);
        }
    }

    private LockData getCurrentLock() {
        return CURRENT_LOCK.get();
    }

    private void setCurrentLock(String path) {
        if (getCurrentLock() == null) {
            CURRENT_LOCK.set(new LockData(path));
            return;
        }
        getCurrentLock().setCurrentPath(path);
        getCurrentLock().getAllPathsInThread().putIfAbsent(path, false);
        //set lock count in thread
        getCurrentLock().getLockCount().set(getCurrentLock().getAllPathsInThread().size());
    }

    private void clearCurrentLock() {
        CURRENT_LOCK.remove();
    }

    @Getter
    @Setter
    class LockData {
        /**
         * key: path, value: locked flag
         */
        private final Map<String, Boolean> allPathsInThread = new HashMap<>();
        private String currentPath;
        private final AtomicInteger lockCount = new AtomicInteger(1);

        LockData(String path) {
            this.allPathsInThread.putIfAbsent(path, false);
            this.currentPath = path;
        }
    }

}
