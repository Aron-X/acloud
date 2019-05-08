package com.aron.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * description:
 * <p>AronSender .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/12        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/12 13:45
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Component
@Slf4j
public class CimRabbitTemplate {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String routingKey, String msg) {
        rabbitTemplate.convertAndSend("MES_ASYNC_EXCHANGE", routingKey, msg);
    }

    public String call(String callRoutingKey, String replyRoutingKey, String msg) {
        Message message = MessageBuilder.withBody((msg).getBytes()).build();
        message.getMessageProperties().setCorrelationId(UUID.randomUUID().toString());
        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
        //set reply routing-key and direct-exchange-name
        message.getMessageProperties().getHeaders().put("direct-exchange-name", "MES_SYNC_EXCHANGE");
        message.getMessageProperties().getHeaders().put("routing-key", replyRoutingKey);
        Object receive = rabbitTemplate.convertSendAndReceive("MES_SYNC_EXCHANGE", callRoutingKey, message);
        if (receive instanceof byte[]) {
            receive = new String((byte[]) receive);
        }
        log.info(" ## reply is : {}", receive);
        return (String) receive;
    }

}
