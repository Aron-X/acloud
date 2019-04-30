package com.aron;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * description:
 * <p>ExternalTest .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/4/24        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/4/24 13:40
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ExternalTest {

    @Autowired
    @Qualifier("syncRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test() {
        log.info(">>> start <<<");
        Object userJson = "{\"user\":{\"userId\":\"aron-test\",\"functionId\":\"RELEASE\",\"password\":\"demoData\",\"newPassword\":\"demoData\"," +
                "\"clientNode\":\"demoData\"},\"functionId\":\"RELEASE\",\"messageBody\":{\"action\":\"create\",\"releaseType\":\"user\"," +
                "\"body\":{\"userId\":\"aron-test\",\"userName\":\"aron-test\",\"company\":\"fa\",\"department\":\"pdc\"," +
                "\"password\":\"test\",\"expiredPeriod\":1,\"supervisorFlag\":true,\"eMailAddress\":\"demoData\",\"phoneNumber\":\"demoData\"," +
                "\"privilegeGroups\":[\"demoData\"],\"pptAreaGroups\":[\"demoData\"],\"brmUserGroups\":[\"demoData\"]," +
                "\"brmReleasePermissionFlag\":true,\"brmReleaseConditionFlag\":true,\"brmDeletePermissionFlag\":true," +
                "\"brmDeleteConditionFlag\":true,\"brmActivatePermissionFlag\":true,\"userDataSets\":{\"type\":\"demoData\",\"value\":\"demoData\"," +
                "\"name\":\"demoData\"}}},\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"\"}";
        Object mfgJson = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"demoData\"},\"functionId\":\"RELEASE\"," +
                "\"messageBody\":{\"action\":\"create\",\"releaseType\":\"mfgLayer\",\"body\":{\"description\":\"demoData\"," +
                "\"userDataSet\":[{\"type\":\"demoData\",\"value\":\"demoData\",\"name\":\"demoData\"}]," +
                "\"mfgLayerId\":\"Mfg Layer\",\"opeCat\":\"demoData\"}}," +
                "\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"2019-04-29 13:51:00\"}";
        Object bankJson = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"demoData\"},\"functionId\":\"RELEASE\"," +
                "\"messageBody\":{\"action\":\"update\",\"releaseType\":\"bank\",\"body\":{\"bankId\":\"bank01\",\"description\":\"this is test3\"," +
                "\"destinationBanks\":[\"bank02\"],\"stocker\":\"\",\"receiveFlag\":true,\"stbFlag\":true,\"bankInFlag\":true," +
                "\"shipFlag\":true,\"productType\":\"test\",\"waferIdAssignmentRequiredFlag\":true,\"productionBankFlag\":true," +
                "\"recycleBankFlag\":true,\"controlWaferBankFlag\":true,\"userDataSets\":[{\"type\":\"demoData\",\"value\":\"demoData\"," +
                "\"name\":\"demoData\"}]}},\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"2019-04-29 13:51:00\"}";
        Object recipeJson = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"demoData\"},\"functionId\":\"RELEASE\"," +
                "\"messageBody\":{\"action\":\"update\",\"releaseType\":\"recipe\",\"body\":{\"recipeId\":\"RCP-01\",\"recipeNameSpace\":\"ARON\"," +
                "\"version\":\"01\",\"description\":\"thsi is test\",\"forceDownloadFlag\":true,\"recipeBodyConfirmFlag\":true," +
                "\"conditionalDownloadFlag\":true,\"fpcCategory\":\"this is test fpc data\",\"whiteDefinitionFlag\":true," +
                "\"fileLocation\":\"demoData\",\"equipments\":null,\"userGroups\":null,\"genericRecipe\":\"demoData\"," +
                "\"userDataSets\":[{\"type\":\"demoData\",\"value\":\"demoData\",\"name\":\"demoData\"}]}}," +
                "\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"2019-04-29 13:51:00\"}";
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 1; i++) {
            executorService.execute(() -> {
                String threadName = Thread.currentThread().getName();
                /*Object reply = rabbitTemplate.convertSendAndReceive("MES_SYNC_EXCHANGE", "MES_SYNC", threadName + " ## hi dude, how are you " +
                        "doing ?");*/
                ///Object message = threadName + " ## hi dude, how are you doing ?";
                Object reply = rabbitTemplate.convertSendAndReceive("MES_SYNC_EXCHANGE", "MES_SYNC", recipeJson
                );
                if (reply instanceof byte[]) {
                    reply = new String((byte[]) reply);
                }
                //new CorrelationData(UUID.randomUUID().toString()
                //rabbitTemplate.ack
                log.info(threadName + " ## reply is :" + reply.toString());
            });
        }
        executorService.shutdown();
        boolean termination = false;
        try {
            termination = executorService.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!termination) {
            executorService.shutdownNow();
        }
    }

    @Test
    public void test1() {
        rabbitTemplate.convertAndSend("MES_ASYNC_EXCHANGE", "MES_ASYNC", "this is another test");
    }
}
