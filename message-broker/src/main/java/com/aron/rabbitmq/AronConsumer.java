package com.aron.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * description:
 * <p>AronConsumer .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/12        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/12 13:49
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Component
@RabbitListener(queues = {"aron"})
@Slf4j
public class AronConsumer {

    @RabbitHandler
    public void process(String msg) {
        log.info(">>>>>>>>> " + msg);
    }
}
