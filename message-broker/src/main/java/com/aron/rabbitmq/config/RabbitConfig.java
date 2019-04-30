package com.aron.rabbitmq.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description:
 * <p>RabbitConfig .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/12        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/12 13:31
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Configuration
public class RabbitConfig {

    @Autowired
    private RabbitProperties rabbitProperties;

    /*@Bean
    public Queue testQueue() {
        return new Queue("aron");
    }*/

    @Bean
    public ConnectionFactory syncConnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
//        ExecutorService service= Executors.newFixedThreadPool(20);//500个线程的线程池
//        connectionFactory.setSharedExecutor(service);
        return connectionFactory;
    }

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(syncConnectionFactory());
        cachingConnectionFactory.setPublisherConfirms(true);
        return cachingConnectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        ExecutorService service = Executors.newFixedThreadPool(600);
        factory.setTaskExecutor(service);
        factory.setConcurrentConsumers(500);
        factory.setPrefetchCount(5);
        configurer.configure(factory, rabbitConnectionFactory());
        return factory;
    }

    @Bean
    public Queue requestQueue() {
        String queueName = rabbitProperties.getRequestQueueName();
        boolean durable = rabbitProperties.isRequestQueueDurable();
        boolean exclusive = rabbitProperties.isRequestQueueExclusive();
        boolean autoDelete = rabbitProperties.isRequestQueueAutoDelete();
        return new Queue(queueName, durable, exclusive, autoDelete);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(requestQueue()).to(requestExchange()).with(rabbitProperties.getSyncRoutingKey());
    }

    @Bean
    public DirectExchange requestExchange() {
        return new DirectExchange(rabbitProperties.getSyncDirectExchangeName(), false, false);
    }

    /**
     * 定义rabbitmqTemplate
     *
     * @return
     *//*
    @Bean
    @Qualifier("syncRabbitTemplate")
    public RabbitTemplate fixedReplyQRabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory());
        template.setExchange(requestExchange().getName());
        template.setRoutingKey(rabbitProperties.getSyncRoutingKey());
        template.setReplyAddress(requestExchange().getName() + "/" + replyQueue().getName());
        //template.setReceiveTimeout(60000);
        template.setReplyTimeout(60000);
        return template;
    }

    @Bean
    @Qualifier("asyncRabbitTemplate")
    public RabbitTemplate asyncRabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory());
//        template.setExchange(new DirectExchange(rabbitProperties.getAsyncDirectExchangeName(), false, true).getName());
        template.setExchange(requestExchange().getName());
        template.setRoutingKey(rabbitProperties.getAsyncRoutingKey());
        return template;
    }


    *//**
     * rabbitmqTemplate监听返回队列的消息
     *
     * @return
     *//*
    @Bean
    public SimpleMessageListenerContainer replyListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory());
        container.setQueues(replyQueue());
        container.setMessageListener(fixedReplyQRabbitTemplate());
        //300个线程的线程池 TODO:Manually create thread pool is better.
        ExecutorService executorService = Executors.newFixedThreadPool(300);
        container.setTaskExecutor(executorService);
        container.setConcurrentConsumers(200);
        container.setPrefetchCount(5);
        return container;
    }

    *//**
     * 请求队列和交换器绑定
     *//*

     *//**
     * 返回队列和交换器绑定
     *//*
    @Bean
    public Binding replyBinding() {
        return BindingBuilder.bind(replyQueue())
                .to(requestExchange())
                .with(replyQueue().getName());
    }

    *//*@Bean
    public Binding aronBinding() {
        return BindingBuilder.bind(testQueue())
                .to(requestExchange())
                .with(testQueue().getName());
    }*//*

     *//**
     * 请求队列
     *
     * @return
     *//*

     *//**
     * 每个应用实例监听的返回队列
     *
     * @return
     *//*
    @Bean
    public Queue replyQueue() {
        String queueName = rabbitProperties.getReplyQueueName() + "-" + UUID.randomUUID().toString();
        boolean durable = rabbitProperties.isReplyQueueDurable();
        boolean exclusive = rabbitProperties.isReplyQueueExclusive();
        boolean autoDelete = rabbitProperties.isReplyQueueAutoDelete();
        return new Queue(queueName, durable, exclusive, autoDelete);
    }

    *//**
     * 交换器
     *
     * @return
     *//*

    @Bean
    public RabbitAdmin admin() {
        return new RabbitAdmin(rabbitConnectionFactory());
    }*/

}
