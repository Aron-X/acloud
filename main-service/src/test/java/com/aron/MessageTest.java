package com.aron;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * description:
 * <p>MessageTest .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/12        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/12 15:50
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MessageTest {

    /*@Autowired
    private SyncProducer syncProducer;*/

   /* @Test
    public void test() {
        String responseValue = syncProducer.call("this is aron sync test, sync!");
        System.out.println("test(): " + responseValue);
    }

    @Test
    public void test2() {
        syncProducer.send("this is aron async test! , async");
    }*/
}
