package com.aron.lock;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry;

/**
 * description:
 * <p>ZkLockConfig .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/1/12        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/1/12 13:55
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Configuration
public class ZkLockConfig {

    private static final int ZOOKEEPER_TIMEOUT = 120000;

    @Value("${spring.application.name}")
    private String serviceName;

    @Bean
    public ZookeeperLockRegistry zookeeperLockRegistry(CuratorFramework client) {
//        CuratorFramework client = CuratorFrameworkFactory.builder().build();
//        CuratorZookeeperClient zookeeperClient = client.getZookeeperClient();
        return new ZookeeperLockRegistry(client, String.format("/%s-%s", serviceName, "lock"));
    }

    /*@Bean
    public void expireUnused(CuratorFramework client) {
        zookeeperLockRegistry(client).expireUnusedOlderThan(ZOOKEEPER_TIMEOUT);
    }*/
}
