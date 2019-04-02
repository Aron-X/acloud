package com.aron.kafka.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * description:
 * This Class use to define the response for all of controller methods.
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/3/20        ********            Bear         create file
 *
 * @author: Bear
 * @date: 2018/3/20 10:18
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Reply implements Serializable {
    private static final Long serialVersionUID = 3341248196426436741L;

    private static final String FORMAT_SPLIT = "##";

    private int code;
    private String functionId;
    private String message;
    private Object body;

}
