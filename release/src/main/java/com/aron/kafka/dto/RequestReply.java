package com.aron.kafka.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * description:
 * <p>MessageDto .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/3/22        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/3/22 15:08
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Getter
@ToString
public class RequestReply implements Serializable {
    private static final long serialVersionUID = -5510626308023961478L;

    private Request request;
    private Reply reply;

    public static RequestReply request(Request request) {
        RequestReply requestReply = new RequestReply();
        requestReply.request = request;
        return requestReply;
    }

    public static RequestReply reply(Reply reply) {
        RequestReply requestReply = new RequestReply();
        requestReply.reply = reply;
        return requestReply;
    }
}
