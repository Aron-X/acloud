package com.aron.kafka.dto;

import lombok.Getter;
import lombok.Setter;

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
@Setter
public class RequestReply {
    private String data;

    private Object request;

    private Object reply;

    public Object getRequest() {
        return request;
    }

    public Object getReply() {
        return reply;
    }

    public static RequestReply request(Object request) {
        RequestReply requestReply = new RequestReply();
        requestReply.request = request;
        return requestReply;
    }

    public static RequestReply reply(Object reply) {
        RequestReply requestReply = new RequestReply();
        requestReply.reply = reply;
        return requestReply;
    }
}
