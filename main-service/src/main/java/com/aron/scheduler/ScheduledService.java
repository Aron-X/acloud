package com.aron.scheduler;

import com.aron.lock.CuratorConfiguration;
import com.aron.lock.OmZookeeperLock;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

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

    @Autowired
    private OmZookeeperLock zookeeperLockRegistry;

    @Autowired
    private CuratorFramework client;

    @Autowired
    private String lockRootPath;

    /**
     * 每天早上6点到晚上9点内每2分钟执行一次
     */
    private static final String EXPIRE_UNUSED_CRON = "0 0/2 6-21 * * *";

    /**
     * 每天凌晨2点执行一次
     */
    private static final String CLEAR_ALL_UNUSED_CRON = "0 0 2 * * ?";


    @Scheduled(cron = EXPIRE_UNUSED_CRON)
    public void clearExpireUnusedPath() {
        log.debug(">>>> expire unused lock node <<<<");
        zookeeperLockRegistry.expireUnusedOlderThan(CuratorConfiguration.ZK_PATH_EXPIRED_TIME);
    }

    @Scheduled(cron = CLEAR_ALL_UNUSED_CRON)
    public void clearAllUnusedPath() {
        GetChildrenBuilder children = client.getChildren();
        if (StringUtils.isEmpty(lockRootPath)) {
            return;
        }
        try {
            List<String> childPaths = children.forPath(lockRootPath);
            if (childPaths == null || childPaths.isEmpty()) {
                return;
            }
            for (String child : childPaths) {
                log.debug("clear child is {}", child);
                client.delete()
                        .inBackground()
                        .forPath(lockRootPath + "/" + child);
            }
        } catch (Exception e) {
            log.error("delete zk path children failed", e);
        }
    }

}
