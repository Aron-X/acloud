package com.aron.kafka.dto.gson;

import lombok.Data;

import java.util.List;

/**
 * description:
 * <p>UserReleaseBean .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/2        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/2 17:10
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Data
public class UserReleaseBean {

    private String action;
    private String releaseType;
    private BodyBean body;

    @Data
    public static class BodyBean {

        private String userId;
        private String userName;
        private String company;
        private String department;
        private String password;
        private int expiredPeriod;
        private boolean supervisorFlag;
        private String eMailAddress;
        private String phoneNumber;
        private boolean brmReleasePermissionFlag;
        private boolean brmReleaseConditionFlag;
        private boolean brmDeletePermissionFlag;
        private boolean brmDeleteConditionFlag;
        private boolean brmActivatePermissionFlag;
        private UserDataSetsBean userDataSets;
        private List<String> privilegeGroups;
        private List<String> pptAreaGroups;
        private List<String> brmUserGroups;

        @Data
        public static class UserDataSetsBean {
            private String type;
            private String value;
            private String name;
        }
    }
}
