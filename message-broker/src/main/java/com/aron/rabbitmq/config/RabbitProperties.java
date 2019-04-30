package com.aron.rabbitmq.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * description:
 * <p>RabbitProperties .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/21        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/21 16:17
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@PropertySource(value = {"classpath:rabbitmq.properties"})
@Getter
@Setter
@Component
public class RabbitProperties {
    /**
     * RabbitMQ host.
     */
    @Value("${rabbitmq.host}")
    private String host = "localhost";

    /**
     * RabbitMQ port.
     */
    @Value("${rabbitmq.port}")
    private int port = 5672;

    /**
     * Login user to authenticate to the broker.
     */
    @Value("${rabbitmq.username}")
    private String username = "guest";

    /**
     * Login to authenticate against the broker.
     */
    @Value("${rabbitmq.password}")
    private String password = "guest";

    /**
     * Virtual host to use when connecting to the broker.
     */
    @Value("${rabbitmq.virtual-host}")
    private String virtualHost;

    /**
     * Comma-separated list of addresses to which the client should connect.
     */
    //private String addresses;

    /**
     * Whether to enable publisher confirms.
     */
    //private boolean publisherConfirms;

    /**
     * Whether to enable publisher returns.
     */
    //private boolean publisherReturns;

    /**
     * Connection timeout. Set it to zero to wait forever.
     */
    //private Duration connectionTimeout = Duration.ofSeconds(60);

    @Value("${rabbitmq.sync.routing-key}")
    private String syncRoutingKey;

    @Value("${rabbitmq.async.routing-key}")
    private String asyncRoutingKey;

    @Value("${rabbitmq.sync.direct-exchange-name}")
    private String syncDirectExchangeName;

    @Value("${rabbitmq.async.direct-exchange-name}")
    private String asyncDirectExchangeName;

    @Value("${rabbitmq.sync.request-queue.name}")
    private String requestQueueName;
    @Value("${rabbitmq.sync.request-queue.durable}")
    private boolean requestQueueDurable;
    @Value("${rabbitmq.sync.request-queue.exclusive}")
    private boolean requestQueueExclusive;
    @Value("${rabbitmq.sync.request-queue.autoDelete}")
    private boolean requestQueueAutoDelete;

    @Value("${rabbitmq.sync.reply-queue.name}")
    private String replyQueueName;
    @Value("${rabbitmq.sync.reply-queue.durable}")
    private boolean replyQueueDurable;
    @Value("${rabbitmq.sync.reply-queue.exclusive}")
    private boolean replyQueueExclusive;
    @Value("${rabbitmq.sync.reply-queue.autoDelete}")
    private boolean replyQueueAutoDelete;

}
