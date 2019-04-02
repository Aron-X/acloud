package com.aron.kafka;

import com.alibaba.fastjson.JSONObject;
import com.aron.kafka.dto.Reply;
import com.aron.kafka.dto.Request;

/**
 * description:
 * <p>JsonTool .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/2        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/2 15:34
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
public final class JsonTool {

    public static Request getRequest(JSONObject jsonObject) {
        return jsonObject.getObject("request", Request.class);
    }

    public static Reply getReply(JSONObject jsonObject) {
        System.out.println("===" + jsonObject.toString());
        return jsonObject.getObject("reply", Reply.class);
    }
}
