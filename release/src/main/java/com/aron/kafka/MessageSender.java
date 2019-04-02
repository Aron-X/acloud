package com.aron.kafka;

import com.alibaba.fastjson.JSONObject;
import com.aron.kafka.dto.Reply;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * description:
 * <p>MessageSender .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/3/22        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/3/22 14:54
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Component
@Slf4j
public class MessageSender {

    @Autowired
    private ReplyingKafkaTemplate<String, JSONObject, JSONObject> kafkaTemplate;

    @Autowired
    private KafkaConfig kafkaConfig;

    public void sendAndReceive(String requestTopic, JSONObject jsonObject) {
        try {
            //String value = JSONObject.toJSONString(JSONObject.getRequest());
            log.info(">>>>>>>>>>>  send value is {}", JsonTool.getRequest(jsonObject));

            ProducerRecord<String, JSONObject> record = new ProducerRecord<>(requestTopic, jsonObject);
            // set reply topic in header
            record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, kafkaConfig.getReplyTopic().getBytes(StandardCharsets.UTF_8)));
            //send
            RequestReplyFuture<String, JSONObject, JSONObject> replyFuture = kafkaTemplate.sendAndReceive(record);
            //check if producer produced successfully
            SendResult<String, JSONObject> sendResult = replyFuture.getSendFuture().get();
            if (sendResult == null) {
                throw new RuntimeException("sendAndReceive failed");
            }
            //get reply result
            ConsumerRecord<String, JSONObject> replyRecord = replyFuture.get();
            Reply reply = JsonTool.getReply(replyRecord.value());
            //JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            log.info(">>>>>>>>>>>  reply message is {}", reply.toString());
        } catch (Exception e) {
            log.error("sendAndReceive failed", e);
        }

    }
}
