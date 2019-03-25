package com.aron.kafka;

import com.alibaba.fastjson.JSONObject;
import com.aron.kafka.dto.RequestReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * description:
 * <p>RequestListener .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/3/22        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/3/22 15:41
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Component
@Slf4j
public class RequestListener {

    /**
     * requestListener
     *
     * @param request
     * @return
     */
    @KafkaListener(topics = "release-request", containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public RequestReply requestListener(RequestReply request) {
        log.info(">>>>>>>>>>>  received message : {}", request);
        JSONObject jsonObject = JSONObject.parseObject(request.getData());
        String name = jsonObject.getString("name");
        log.info(">>>>>>>>>>>  name : {}", name);
        //process
        jsonObject.replace("name", "processed name");
        request.setData(jsonObject.toJSONString());

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return request;
    }
}
