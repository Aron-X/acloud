package com.aron.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * description:
 * This Class use to define the reply for all of controller methods.
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
//@ToString
@JSONType(ignores = {"serialVersionUID", "FORMAT_SPLIT"})
public class Response implements Serializable {
    private static final Long serialVersionUID = 3341248196426436741L;

    private static final String FORMAT_SPLIT = "##";

    public static final int SUCCESS = 0;

    private Integer code;
    private String transactionID;
    private String message;
    private Object body;

    /**
     * description:
     * <p>
     * change history:
     * date             defect#             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param code
     * @param transactionID
     * @param message
     * @param body
     * @return
     * @author Bear
     * @date 2018/3/20
     */
    public Response(Integer code, String transactionID, String message, Object body) {
        this.code = code;
        this.transactionID = transactionID;
        this.message = message;
        this.body = body;
    }

    /**
     * description:
     * <p>
     * change history:
     * date             defect#             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param code          -
     * @param transactionID -
     * @param message       -
     * @param body          -
     * @return com.fa.cim.dto.Response
     * @author Bear
     * @date 2018/3/20
     */
    public static Response create(Integer code, String transactionID, String message, Object body) {
        return new Response(code, transactionID, message, body);
    }

    /**
     * description:
     * <p>
     * change history:
     * date             defect#             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param code          -
     * @param transactionID -
     * @param body          -
     * @param message       -
     * @return com.fa.cim.dto.Response
     * @author Bear
     * @date 2018/3/20
     */
    public static Response createWarn(Integer code, String transactionID, Object body, String message) {
        return new Response(code, transactionID, message, body);
    }

    /**
     * description:
     * <p>
     * change history:
     * date             defect#             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param code          -
     * @param transactionID -
     * @param message       -
     * @return com.fa.cim.dto.Response
     * @author Bear
     * @date 2018/3/20
     */
    public static Response createError(final Integer code, final String transactionID, final String message) {
        return new Response(code, transactionID, message, null);
    }


    /**
     * description:
     * change history:
     * date             defect             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @return java.lang.String
     * @author Bear
     * @date 2018/6/29
     */
    @Override
    public String toString() {
        return String.format("{\"code\":%d, \"transactionID\":\"%s\", \"message\":\"%s\", \"body\":%s}",
                code, transactionID, message, body);
    }
}
