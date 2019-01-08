package com.aron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * description:
 * <p>CustomAdditionData .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/1/3        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/1/3 10:12
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Component
public class CustomAdditionData {

    @Autowired
    @Qualifier("customDataWithNo")
    private CustomData customData;

    public CustomData getCustomData() {
        return this.customData;
    }
}
