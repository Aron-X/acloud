package com.aron.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Address;
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
        Message message = MessageBuilder.withBody((msg).getBytes())
                .setReplyToAddress(new Address("MES_SYNC_EXCHANGE", replyRoutingKey))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setCorrelationId(UUID.randomUUID().toString())
                .build();
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("## ack confirm -> call send successed ##");
                return;
            }
            log.info("## ack confirm -> call send failed : {} ,{} ##", cause, correlationData.toString());
        });
        rabbitTemplate.setReturnCallback((message1, replyCode, replyText, exchange, routingKey) -> log.info("## return-message:" + new String(message1.getBody()) + ",replyCode:" + replyCode
                + ",replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey));
        Object receive = rabbitTemplate.convertSendAndReceive("MES_SYNC_EXCHANGE", callRoutingKey, message,
                new CorrelationData(UUID.randomUUID().toString()));
        if (receive instanceof byte[]) {
            receive = new String((byte[]) receive);
        }
        log.info(" ## reply is : {}", receive);
        return (String) receive;
    }

}
