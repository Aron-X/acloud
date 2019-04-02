package com.aron.kafka.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

/**
 * description:
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/3/20        ********            Bear         create file
 *
 * @author: Bear
 * @date: 2018/3/20 10:18
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MessageCode {
    public static final int SUCCESS_CODE = 0;
    public static final int WARNING_CODE = 1;
    public static final int ERROR_CODE = 2;
    public static final int SYSTEM_ERROR = 2037;

    private int code;
    private String message;

    public MessageCode(String temp) {
        if (null == temp) {
            return;
        }
        int splitIndex = temp.indexOf(",");
        this.code = Integer.parseInt(temp.substring(1, splitIndex).trim());
        int index = temp.indexOf("\"");

        final int notFound = -1;
        if (notFound == index) {
            this.message = temp.substring(splitIndex + 1, temp.length() - 1).trim();
        } else {
            this.message = temp.substring(index + 1, temp.lastIndexOf("\"")).trim();
        }
    }

    public MessageCode(int pCode, String pMessage) {
        this.code = pCode;
        this.message = pMessage;
    }

    public MessageCode(MessageCode cimCode, String... args) {
        String tempMessage = cimCode.getMessage();
        this.code = cimCode.getCode();
        this.message = (args.length >= count(tempMessage, "%s")) ? String.format(tempMessage, args) : tempMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MessageCode target = (MessageCode) o;
        return code == target.code;
    }

    /**
     * description:
     * use to code equals check
     * change history:
     * date             defect             person             comments
     * ---------------------------------------------------------------------------------------------------------------------
     *
     * @param target target
     * @return boolean boolean
     * @author PlayBoy
     * @date 2018/8/8
     */
    public boolean equals(MessageCode target) {
        if (target == null) {
            return false;
        }
        return this.code == target.getCode();
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + message.hashCode();
        return result;
    }

    /**
     * description:
     * change history:
     * date             defect             person             comments
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @param source    -
     * @param subString -
     * @return int
     * @author Bear
     * @date 2019/2/1 13:14
     */
    private int count(String source, String subString) {
        int count = 0;
        if (StringUtils.isEmpty(source) || StringUtils.isEmpty(subString)) {
            return count;
        }
        int index = source.indexOf(subString);
        while (-1 != index) {
            count = count + 1;
            index = source.indexOf(subString, index + 1);
        }
        return count;
    }
}
