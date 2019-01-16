package com.aron.lock;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource(value = "classpath:zookeeper.properties")
@ConfigurationProperties(prefix = "zk")
@Configuration
@Slf4j
public class CuratorConfiguration {

//    public static final long ZOOKEEPER_TIMEOUT = 120000;

    @Value("${spring.application.name}")
    private String serviceName;

    @Setter
    private String suffixPath;

    @Setter
    private String connectString;

    @Setter
    private int retryTimes;

    @Setter
    private int sleepBetweenRetries;

    @Setter
    private int sessionTimeoutMs;

    @Setter
    private int connectionTimeoutMs;

    /**
     * ZookeeperLockRegistry initial
     *
     * @param client       client
     * @param lockRootPath lockRootPath
     * @return ZookeeperLockRegistry
     */
    @Bean
    public ZookeeperLockRegistry zookeeperLockRegistry(CuratorFramework client, @Qualifier("lockRootPath") String lockRootPath) {
        return new ZookeeperLockRegistry(client, lockRootPath);
    }

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        log.info(">>>>> zookeeper connectString:{}, sessionTimeoutMs:{}, connectionTimeoutMs:{}, retryTimes:{}, sleepBetweenRetries:{}",
                connectString, sessionTimeoutMs, connectionTimeoutMs, retryTimes, sleepBetweenRetries);
        return CuratorFrameworkFactory.newClient(
                connectString,
                sessionTimeoutMs,
                connectionTimeoutMs,
                new RetryNTimes(retryTimes, sleepBetweenRetries));
    }

    @Bean("lockRootPath")
    public String getLockRoot() {
        return String.format("/%s-%s", serviceName, suffixPath);
    }
}
