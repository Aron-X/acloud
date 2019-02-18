package com.aron.lock;

import com.aron.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.integration.support.locks.ExpirableLockRegistry;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.concurrent.ExecutorConfigurationSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * description:
 * <p>OmZookeeperLock .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/2/14        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/2/14 15:29
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Slf4j
public class OmZookeeperLock implements ExpirableLockRegistry, DisposableBean {

    private static final String DEFAULT_ROOT = "/Spring-LockRegistry";

    private final CuratorFramework client;

    private final OmZookeeperLock.KeyToPathStrategy keyToPath;

    private final Map<String, OmZookeeperLock.ZkLock> locks = new ConcurrentHashMap<>();

    private final boolean trackingTime;

    private AsyncTaskExecutor mutexTaskExecutor = new ThreadPoolTaskExecutor();

    {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) this.mutexTaskExecutor;
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        threadPoolTaskExecutor.setBeanName("ZookeeperLockRegistryExecutor");
        threadPoolTaskExecutor.initialize();
    }

    private boolean mutexTaskExecutorExplicitlySet;

    /**
     * Construct a lock registry using the default {@link OmZookeeperLock.KeyToPathStrategy} which
     * simple appends the key to '/SpringIntegration-LockRegistry/'.
     *
     * @param client the {@link CuratorFramework}.
     */
    public OmZookeeperLock(CuratorFramework client) {
        this(client, DEFAULT_ROOT);
    }

    /**
     * Construct a lock registry using the default {@link OmZookeeperLock.KeyToPathStrategy} which
     * simple appends the key to {@code '<root>/'}.
     *
     * @param client the {@link CuratorFramework}.
     * @param root   the path root (no trailing /).
     */
    public OmZookeeperLock(CuratorFramework client, String root) {
        this(client, new OmZookeeperLock.DefaultKeyToPathStrategy(root));
    }

    /**
     * Construct a lock registry using the supplied {@link OmZookeeperLock.KeyToPathStrategy}.
     *
     * @param client    the {@link CuratorFramework}.
     * @param keyToPath the implementation of {@link OmZookeeperLock.KeyToPathStrategy}.
     */
    public OmZookeeperLock(CuratorFramework client, OmZookeeperLock.KeyToPathStrategy keyToPath) {
        Assert.notNull(client, "'client' cannot be null");
        Assert.notNull(client, "'keyToPath' cannot be null");
        this.client = client;
        this.keyToPath = keyToPath;
        this.trackingTime = !keyToPath.bounded();
    }

    /**
     * Set an {@link AsyncTaskExecutor} to use when establishing (and testing) the
     * connection with Zookeeper. This must be performed asynchronously so the
     * {@link Lock#tryLock(long, TimeUnit)} contract can be honored. While an executor is
     * used internally, an external executor may be required in some environments, for
     * example those that require the use of a {@code WorkManagerTaskExecutor}.
     *
     * @param mutexTaskExecutor the executor.
     * @since 4.2.10
     */
    public void setMutexTaskExecutor(AsyncTaskExecutor mutexTaskExecutor) {
        Assert.notNull(mutexTaskExecutor, "'mutexTaskExecutor' cannot be null");
        ((ExecutorConfigurationSupport) this.mutexTaskExecutor).shutdown();
        this.mutexTaskExecutor = mutexTaskExecutor;
        this.mutexTaskExecutorExplicitlySet = true;
    }

    @Override
    public Lock obtain(Object lockKey) {
        if (lockKey instanceof BaseEntity) {
            String type = lockKey.getClass().getSimpleName();
            lockKey = String.format("Object-%s-%s", type, ((BaseEntity) lockKey).getId());
        }
        Assert.isInstanceOf(String.class, lockKey);
        String path = this.keyToPath.pathFor((String) lockKey);
        OmZookeeperLock.ZkLock lock = this.locks.computeIfAbsent(path, p -> new OmZookeeperLock.ZkLock(this.client, this.mutexTaskExecutor, p));
        if (this.trackingTime) {
            lock.setLastUsed(System.currentTimeMillis());
        }
        return lock;
    }

    public Optional<CollectionLock> tryLockAll(Collection<?> arrays) {
        if (arrays == null || arrays.isEmpty()) {
            return Optional.empty();
        }
        CollectionLock collectionLock = new CollectionLock();
        collectionLock.setObtainAll(true);
        List<Lock> itemLocks = new ArrayList<>();
        collectionLock.setItemLocks(itemLocks);
        try {
            for (Object lockKey : arrays) {
                Lock itemLock = obtain(lockKey);
                boolean locked = itemLock.tryLock(500, TimeUnit.MILLISECONDS);
                if (!locked) {
                    collectionLock.setObtainAll(false);
                    break;
                }
                itemLocks.add(itemLock);
            }
        } catch (Exception e) {
            log.error("tryLockAll failed", e);
        } finally {
            if (!collectionLock.isObtainAll()) {
                itemLocks.forEach(Lock::unlock);
            }
        }
        return Optional.of(collectionLock);
    }

    @Getter
    public static final class CollectionLock {
        private List<Lock> itemLocks;
        private boolean obtainAll;

        void setItemLocks(List<Lock> itemLocks) {
            this.itemLocks = itemLocks;
        }

        void setObtainAll(boolean obtainAll) {
            this.obtainAll = obtainAll;
        }
    }

    public void unLockAll(List<Lock> locks) {
        if (locks == null || locks.isEmpty()) {
            return;
        }
        for (Lock lockItem : locks) {
            lockItem.unlock();
        }
    }

    /**
     * Remove locks last acquired more than 'age' ago that are not currently locked.
     * Expiry is not supported if the {@link OmZookeeperLock.KeyToPathStrategy} is bounded (returns a finite
     * number of paths). With such a {@link OmZookeeperLock.KeyToPathStrategy}, the overhead of tracking when
     * a lock is obtained is avoided.
     *
     * @param age the time since the lock was last obtained.
     */
    @Override
    public void expireUnusedOlderThan(long age) {
        if (!this.trackingTime) {
            throw new IllegalStateException("Ths KeyToPathStrategy is bounded; expiry is not supported");
        }
        Iterator<Map.Entry<String, OmZookeeperLock.ZkLock>> iterator = this.locks.entrySet().iterator();
        long now = System.currentTimeMillis();
        while (iterator.hasNext()) {
            Map.Entry<String, OmZookeeperLock.ZkLock> entry = iterator.next();
            OmZookeeperLock.ZkLock lock = entry.getValue();
            if (now - lock.getLastUsed() > age
                    && !lock.isAcquiredInThisProcess()) {
                iterator.remove();
                log.debug("remove expire lock is : {}", lock.getPath());
                //fix Curator version bug:  not support CONTAINER type caused created path as persistent
                try {
                    client.delete()
                            .inBackground()
                            .forPath(lock.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        if (!this.mutexTaskExecutorExplicitlySet) {
            ((ExecutorConfigurationSupport) this.mutexTaskExecutor).shutdown();
        }
    }

    /**
     * Strategy to convert a lock key (e.g. aggregation correlation id) to a
     * Zookeeper path.
     */
    @FunctionalInterface
    public interface KeyToPathStrategy {

        /**
         * Return the path for the key.
         *
         * @param key the key.
         * @return the path.
         */
        String pathFor(String key);

        /**
         * @return true if this strategy returns a bounded number of locks, removing
         * the need for removing LRU locks.
         */
        default boolean bounded() {
            return true;
        }

    }

    private static final class DefaultKeyToPathStrategy implements OmZookeeperLock.KeyToPathStrategy {

        private final String root;

        DefaultKeyToPathStrategy(String rootPath) {
            Assert.notNull(rootPath, "'rootPath' cannot be null");
            if (!rootPath.endsWith("/")) {
                this.root = rootPath + "/";
            } else {
                this.root = rootPath;
            }
        }

        @Override
        public String pathFor(String key) {
            return this.root + key;
        }

        @Override
        public boolean bounded() {
            return false;
        }

    }

    private static final class ZkLock implements Lock {

        private final CuratorFramework client;

        private final InterProcessMutex mutex;

        private final AsyncTaskExecutor mutexTaskExecutor;

        @Getter
        private final String path;

        @Getter
        @Setter
        private long lastUsed;

        ZkLock(CuratorFramework client, AsyncTaskExecutor mutexTaskExecutor, String path) {
            this.client = client;
            this.mutex = new InterProcessMutex(client, path);
            this.mutexTaskExecutor = mutexTaskExecutor;
            this.path = path;
        }

        @Override
        public void lock() {
            try {
                this.mutex.acquire();
            } catch (Exception e) {
                throw new RuntimeException("Failed to acquire mutex at " + this.path, e);
            }
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            boolean locked = false;
            // this is a bit ugly, but...
            while (!locked) {
                locked = tryLock(1, TimeUnit.SECONDS);
            }

        }

        @Override
        public boolean tryLock() {
            try {
                return tryLock(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            Future<Boolean> future = null;
            try {
                long startTime = System.currentTimeMillis();

                future = this.mutexTaskExecutor.submit(new Callable<Boolean>() {

                    @Override
                    public Boolean call() throws Exception {
                        return OmZookeeperLock.ZkLock.this.client.checkExists().forPath("/") != null;
                    }

                });

                long waitTime = unit.toMillis(time);

                boolean connected = future.get(waitTime, TimeUnit.MILLISECONDS);

                if (!connected) {
                    future.cancel(true);
                    return false;
                } else {
                    waitTime = waitTime - (System.currentTimeMillis() - startTime);
                    return this.mutex.acquire(waitTime, TimeUnit.MILLISECONDS);
                }
            } catch (TimeoutException e) {
                future.cancel(true);
                return false;
            } catch (Exception e) {
                throw new MessagingException("Failed to acquire mutex at " + this.path, e);
            }
        }

        @Override
        public void unlock() {
            try {
                this.mutex.release();
            } catch (Exception e) {
                throw new MessagingException("Failed to release mutex at " + this.path, e);
            }
        }

        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Conditions are not supported");
        }

        public boolean isAcquiredInThisProcess() {
            return this.mutex.isAcquiredInThisProcess();
        }

    }
}
