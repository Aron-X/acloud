package com.aron.kafka.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * description:
 * <p>Request .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/2        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/2 10:54
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Getter
@Setter
@ToString
public class Request implements Serializable {
    private static final long serialVersionUID = -7909947580678763969L;
    private RequestUser user;
    private String functionId;
    private Object messageBody;
    private String messageId;
    private String sendTime;
}
