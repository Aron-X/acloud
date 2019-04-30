package com.aron.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
public class SyncProducer {

    /*@Autowired
    private AmqpTemplate amqpTemplate;*/

    /*@Autowired
    @Qualifier("syncRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("asyncRabbitTemplate")
    private RabbitTemplate asyncRabbitTemplate;

    public void send(String msg) {
        //amqpTemplate.convertAndSend("aron", msg);
        asyncRabbitTemplate.convertAndSend("aron", msg);
    }

    public String call(String msg) {
        Object receive = rabbitTemplate.convertSendAndReceive(msg);
        System.out.println(">>> call : " + receive);
        return (String) receive;
    }*/
}
