package com.aron.kafka.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description:
 * <p>UserDto .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/2        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/2 10:56
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Data
public class RequestUser implements Serializable {
    private static final long serialVersionUID = -8970870443250270500L;
    /**
     * User ID
     */
    private String userId;
    /**
     * Password
     */
    private String password;
    /**
     * New Password
     */
    private String newPassword;
    /**
     * Function ID. For example, the Function ID of TxEnhancedFutureHoldReq is "TXPC041".
     */
    private String functionId;
    /**
     * Client Node
     */
    private String clientNode;
    /**
     * Reserved for SI customization
     */
    private Object reserve;

    public RequestUser() {

    }

    public RequestUser(String userId) {
        this.userId = userId;
    }
}
