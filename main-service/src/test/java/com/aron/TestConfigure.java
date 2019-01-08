package com.aron;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description:
 * <p>TestConfigure .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/1/3        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/1/3 09:59
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Configuration
public class TestConfigure {

    @Bean("customDataWithYes")
    public CustomData customDataWithYes() {
        System.out.println(">>>>>>>>>>>>>>>>>>> init CustomData Yes <<<<<<<<<<<<<<<<<<<<<");
        return new CustomData(true);
    }

    @Bean("customDataWithNo")
    public CustomData customDataWithNo() {
        System.out.println(">>>>>>>>>>>>>>>>>>> init CustomData No <<<<<<<<<<<<<<<<<<<<<");
        return new CustomData(false);
    }
}
