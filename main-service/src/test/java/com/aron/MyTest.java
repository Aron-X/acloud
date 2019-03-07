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
    @Qualifier("customDataWithNo")
    private CustomData customData1;

    @Autowired
    @Qualifier("customDataWithNo")
    private CustomData customData2;

    @Autowired
    private CustomAdditionData customAdditionData;

    @Test
    public void test1() {
        customData.test();
        customData1.test();
        customData2.test();

        System.out.println(customAdditionData.getCustomData() == customData);
        System.out.println(customData == customData1);
        System.out.println(customData1 == customData2);
    }


}
