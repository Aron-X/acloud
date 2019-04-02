package com.aron.kafka;

import com.aron.kafka.dto.Reply;
import com.aron.kafka.dto.Request;
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
     * @param requestReply requestReply
     * @return RequestReply
     */
    @KafkaListener(topics = "release-request", containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public RequestReply requestListener(RequestReply requestReply) {
        log.info(">>>>>>>>>>>  received message : {}", requestReply.getRequest());
        //JSONObject jsonObject = JSONObject.parseObject();
        Request request = requestReply.getRequest();
        log.info(">>>>>>>>>>>  request : {}", request);
        /*String name = jsonObject.getString("name");
        log.info(">>>>>>>>>>>  name : {}", name);
        //process
        jsonObject.replace("name", "processed name");
        request.setData(jsonObject.toJSONString());*/
        Reply reply = new Reply();
        reply.setMessage("success");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //return RequestReply.reply(reply);
        /*RequestReply RequestReply = new RequestReply();
        RequestReply.ReplyBean replyBean = new RequestReply.ReplyBean();
        replyBean.setMessage("lalallalla");
        RequestReply.setReply(replyBean);*/
        return RequestReply.reply(reply);
    }
}
