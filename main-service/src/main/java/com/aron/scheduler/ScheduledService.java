package com.aron.scheduler;

import com.aron.lock.CuratorConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * description:
 * <p>ScheduledService .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/10/13        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/10/13 18:46
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Slf4j
@Component
public class ScheduledService {

    //@Scheduled(cron = "0/60 * * * * *")
    public void scheduled() {
        log.info(">>>> expire unused lock node <<<<");
    }

    //@Scheduled(cron = "0/1 * * * * *")
    public void removeUnusedPath() {
        /*log.info("lockRegistry :{}", lockRegistry.toString());
        GetChildrenBuilder children = zkClient.getChildren();
        try {
            List<String> childPaths = children.forPath(zkLockConfig.getLockRoot());
            if (childPaths == null || childPaths.isEmpty()) {
                return;
            }
            for (String child : childPaths) {
                log.info("child is {}", child);
                Lock lock = lockRegistry.obtain(child);
                boolean locked = lock.tryLock();
                if (!locked) {
                    log.info("locked: {} ,removed child is {}", locked, child);
                    zkClient.delete()
                            .deletingChildrenIfNeeded()
                            .inBackground()
                            .forPath(zkLockConfig.getLockRoot() + "/" + child);
                }
            }
        } catch (Exception e) {
            log.error("delete zk path children failed", e);
        }*/
    }

}
