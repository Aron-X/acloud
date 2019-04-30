package com.aron.rabbitmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * description:
 * <p>RabbitRequest .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/21        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/21 17:01
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Slf4j
@Component
public class RabbitRequest {

    @RabbitListener(queues = "CIM-REQUEST", containerFactory = "containerFactory")
    public String process(String msg) {
        int sleepTime = (int) (Math.random() * 10000);
        log.info("sleep ：" + sleepTime);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("我收到了：" + msg);
        return msg + " > 已收到，谢谢";
    }

}
