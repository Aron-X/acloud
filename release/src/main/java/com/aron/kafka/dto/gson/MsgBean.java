package com.aron.kafka.dto.gson;

import lombok.Data;

import java.io.Serializable;

/**
 * description:
 * <p>MsgBean .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/2        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/2 13:29
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Data
public class MsgBean implements Serializable {

    private static final long serialVersionUID = 3354906288969423726L;
    /**
     * request : {"user":{"userId":"demoData","functionId":"demoData","password":"demoData","newPassword":"demoData","clientNode":"demoData"}
     * ,"functionId":"TX0001","messageBody":{},"messageId":"78d86cac-e314-473e-aed2-69fe487e2a0a","sendTime":"YYYY-MM-DD hh:mm:ss"}
     * reply : {"code":1,"functionId":"TX001","message":"success"}
     */

    private RequestBean request;
    private ReplyBean reply;

    @Data
    public static class RequestBean implements Serializable {

        private static final long serialVersionUID = 5051361514856522682L;
        /**
         * user : {"userId":"demoData","functionId":"demoData","password":"demoData","newPassword":"demoData","clientNode":"demoData"}
         * functionId : TX0001
         * messageBody : {}
         * messageId : 78d86cac-e314-473e-aed2-69fe487e2a0a
         * sendTime : YYYY-MM-DD hh:mm:ss
         */

        private UserBean user;
        private String functionId;
        private Object messageBody;
        private String messageId;
        private String sendTime;

        @Data
        public static class UserBean implements Serializable {
            private static final long serialVersionUID = 1662851260747006381L;
            /**
             * userId : demoData
             * functionId : demoData
             * password : demoData
             * newPassword : demoData
             * clientNode : demoData
             */
            private String userId;
            private String functionId;
            private String password;
            private String newPassword;
            private String clientNode;

            public UserBean(String userId) {
                this.userId = userId;
            }

        }

    }

    @Data
    public static class ReplyBean implements Serializable {

        private static final long serialVersionUID = 6770601529213199050L;
        /**
         * code : 1
         * functionId : TX001
         * message : success
         */

        private int code;
        private String functionId;
        private String message;
    }
}
