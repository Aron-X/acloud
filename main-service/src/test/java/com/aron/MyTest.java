package com.aron;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * description:
 * <p>MyTest .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/1/3        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/1/3 10:01
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {

    @Autowired
    @Qualifier("customDataWithNo")
    private CustomData customData;

    @Autowired
    private CustomAdditionData customAdditionData;

    @Test
    public void test1() {
        customData.test();

        System.out.println(customAdditionData.getCustomData() == customData);
    }
}
