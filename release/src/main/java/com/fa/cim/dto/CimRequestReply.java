package com.fa.cim.dto;

import com.aron.kafka.dto.Reply;
import com.aron.kafka.dto.Request;
import com.aron.kafka.dto.RequestReply;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * description:
 * <p>CimRequestReply .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/2        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/2 15:06
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Data
@ToString
public class CimRequestReply implements Serializable {

    private static final long serialVersionUID = 246337534425516200L;

    private Request request;
    private Reply reply;

    public static CimRequestReply request(Request request) {
        CimRequestReply requestReply = new CimRequestReply();
        requestReply.request = request;
        return requestReply;
    }

    public static CimRequestReply reply(Reply reply) {
        CimRequestReply requestReply = new CimRequestReply();
        requestReply.reply = reply;
        return requestReply;
    }
}
