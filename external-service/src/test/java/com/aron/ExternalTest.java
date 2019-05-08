package com.aron;

import com.alibaba.fastjson.JSONObject;
import com.aron.dto.Response;
import com.aron.rabbitmq.CimRabbitTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
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
import java.util.concurrent.atomic.AtomicReference;

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
    private CimRabbitTemplate rabbitTemplate;

    @Test
    public void test() {
        log.info(">>> start <<<");
        String userJson = "{\"user\":{\"userId\":\"aron-test\",\"functionId\":\"RELEASE\",\"password\":\"demoData\",\"newPassword\":\"demoData\"," +
                "\"clientNode\":\"demoData\"},\"functionId\":\"RELEASE\",\"messageBody\":{\"action\":\"create\",\"releaseType\":\"user\"," +
                "\"body\":{\"userId\":\"aron\",\"userName\":\"aron\",\"company\":\"fa\",\"department\":\"pdc\"," +
                "\"password\":\"aron\",\"expiredPeriod\":1,\"supervisorFlag\":true,\"eMailAddress\":\"demoData\",\"phoneNumber\":\"demoData\"," +
                "\"privilegeGroups\":[\"demoData\"],\"pptAreaGroups\":[\"demoData\"],\"brmUserGroups\":[\"demoData\"]," +
                "\"brmReleasePermissionFlag\":true,\"brmReleaseConditionFlag\":true,\"brmDeletePermissionFlag\":true," +
                "\"brmDeleteConditionFlag\":true,\"brmActivatePermissionFlag\":true,\"userDataSets\":{\"type\":\"demoData\",\"value\":\"demoData\"," +
                "\"name\":\"demoData\"}}},\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"\"}";
        String equipmentStateJson = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"demoData\"}," +
                "\"functionId\":\"RELEASE\",\"messageBody\":{\"action\":\"update\",\"releaseType\":\"equipmentState\"," +
                "\"body\":{\"equipmentStateCode\":\"ARONCD\",\"equipmentStateName\":\"AronCode\",\"eqpStateDesc\":\"this is test3\"," +
                "\"e10State\":\"ENG\",\"equipmentAvailableFlag\":true,\"conditionalAvailableFlag\":true,\"manufacturingStateChangeableFlag\":true," +
                "\"changeFromOtherE10Flag\":true,\"changeToOtherE10Flag\":true,\"availableSubLotTypes\":null,\"convertingConditions\":null," +
                "\"nextTransitionStates\":null,\"userDataSets\":null}},\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\"," +
                "\"sendTime\":\"2019-04-29 13:51:00\"}";
        String mfgJson = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"demoData\"},\"functionId\":\"RELEASE\"," +
                "\"messageBody\":{\"action\":\"create\",\"releaseType\":\"mfgLayer\",\"body\":{\"description\":\"demoData\"," +
                "\"userDataSet\":[{\"type\":\"demoData\",\"value\":\"demoData\",\"name\":\"demoData\"}]," +
                "\"mfgLayerId\":\"Mfg Layer\",\"opeCat\":\"demoData\"}}," +
                "\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"2019-04-29 13:51:00\"}";
        String bankJson = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"demoData\"},\"functionId\":\"RELEASE\"," +
                "\"messageBody\":{\"action\":\"update\",\"releaseType\":\"bank\",\"body\":{\"bankId\":\"bank01\",\"description\":\"this is test3\"," +
                "\"destinationBanks\":[\"bank02\"],\"stocker\":\"\",\"receiveFlag\":true,\"stbFlag\":true,\"bankInFlag\":true," +
                "\"shipFlag\":true,\"productType\":\"test\",\"waferIdAssignmentRequiredFlag\":true,\"productionBankFlag\":true," +
                "\"recycleBankFlag\":true,\"controlWaferBankFlag\":true,\"userDataSets\":[{\"type\":\"demoData\",\"value\":\"demoData\"," +
                "\"name\":\"demoData\"}]}},\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"2019-04-29 13:51:00\"}";
        String recipeJson = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"demoData\"},\"functionId\":\"RELEASE\"," +
                "\"messageBody\":{\"action\":\"update\",\"releaseType\":\"recipe\",\"body\":{\"recipeId\":\"RCP-01.01\"," +
                "\"recipeNameSpace\":\"ARON\"," +
                "\"version\":\"01\",\"description\":\"thsi is test\",\"forceDownloadFlag\":true,\"recipeBodyConfirmFlag\":true," +
                "\"conditionalDownloadFlag\":true,\"fpcCategory\":\"this is test fpc data\",\"whiteDefinitionFlag\":true," +
                "\"fileLocation\":\"demoData\",\"equipments\":null,\"userGroups\":null,\"genericRecipe\":\"demoData\"," +
                "\"userDataSets\":[{\"type\":\"demoData\",\"value\":\"demoData\",\"name\":\"demoData\"}]}}," +
                "\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"2019-04-29 13:51:00\"}";

        String dcDef = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"aron\"},\"functionId\":\"RELEASE\"," +
                "\"messageBody\":{\"action\":\"update\",\"releaseType\":\"dataCollectionDef\",\"body\":{\"dcDefinitionId\":\"DC_ARON_CJ.00\"," +
                "\"description\":\"this is test\",\"dcType\":\"Process\",\"fpcCategory\":null,\"whiteDefinitionFlag\":false," +
                "\"dcItems\":[{\"itemType\":\"Raw\",\"measurementType\":\"demoData\",\"dcItemName\":\"DC Item1\",\"waferPosition\":\"5\"," +
                "\"sitePosition\":null,\"dataType\":\"String\",\"unit\":null,\"calculationType\":\"Raw\",\"calculationTypeFlag\":false," +
                "\"calculationExpression\":null,\"dcMode\":\"Manual\",\"historyRequiredFlag\":false}],\"userDataSets\":null}}," +
                "\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"2019-04-29 13:51:00\"}";

        String logicalRecipe = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"aron\"},\"functionId\":\"RELEASE\"," +
                "\"messageBody\":{\"action\":\"update\",\"releaseType\":\"logicalRecipe\",\"body\":{\"logicalRecipeId\":\"CMP_ARON.01\"," +
                "\"version\":\"01\",\"description\":\"this is test2\",\"multipleChamberSupportFlag\":false,\"recipeType\":\"process\"," +
                "\"fpcCategory\":null,\"whiteDefinitionFlag\":false,\"testType\":null,\"defaultRecipeSettings\":[{\"recipe\":\"RCP-01.01\"," +
                "\"processResourceStates\":[{\"processResourceName\":\"CH-A\",\"state\":true}],\"recipeParameters\":[{\"name\":\"Param1\"," +
                "\"unit\":\"c\",\"dataType\":\"Integer\",\"defaultValue\":\"60\",\"lowerLimit\":\"20\",\"upperLimit\":\"200\"," +
                "\"useCurrentValueFlag\":false,\"tag\":null},{\"name\":\"Param2\",\"unit\":\"c\",\"dataType\":\"Integer\",\"defaultValue\":\"30\"," +
                "\"lowerLimit\":\"10\",\"upperLimit\":\"100\",\"useCurrentValueFlag\":false,\"tag\":null}],\"dcDefinition\":\"DC_ARON_CJ.00\"," +
                "\"dcSpec\":null,\"binDefinition\":null,\"sampleSpec\":null,\"fixtureGroups\":null}],\"equipmentSpecificRecipeSettings\":null," +
                "\"monitorProduct\":null,\"monitorHoldFlag\":false,\"subLotType\":null,\"userDataSets\":null}}," +
                "\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"2019-04-29 13:51:00\"}";

        String equipmentJson = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"aron\"},\"functionId\":\"RELEASE\"," +
                "\"messageBody\":{\"action\":\"update\",\"releaseType\":\"equipment\",\"body\":{\"equipmentId\":\"ARON0101\",\"description\":\"this" +
                " is test\",\"instanceName\":null,\"equipmentOwner\":\"aron\",\"maxReticleCapacity\":1,\"takeOutInTransferFlag\":false," +
                "\"batchInNotificationRequiredFlag\":false,\"eqpToEqpTransferFlag\":false,\"reticleRequiredFlag\":false," +
                "\"fixtureRequiredFlag\":false,\"carrierChangeRequiredFlag\":false,\"carrierIdReadableFlag\":false,\"waferMapCheckableFlag\":false," +
                "\"waferIdReadableFlag\":false,\"emptyCarrierEarlyOutFlag\":false,\"equipmentCategoryId\":\"Process\"," +
                "\"multipleRecipeCapability\":null,\"equipmentType\":\"process\",\"recipeBodyManageFlag\":false," +
                "\"processBatchAvailableFlag\":false,\"processBatchUnit\":null,\"minimumProcessBatchSize\":1,\"maximumProcessBatchSize\":1," +
                "\"minimumWaferCount\":1,\"underTrackStoragePriorityFlag\":false,\"cellControllerNode\":null,\"supplierName\":null," +
                "\"modelNumber\":null,\"serialNumber\":null,\"monitorCreationFlag\":false,\"maximumRunWafers\":1,\"maximumRunTime\":1," +
                "\"maximumStartCount\":1,\"pmIntervalTime\":1,\"standardWPH\":1,\"rawEquipmentStateSetId\":null," +
                "\"initialEquipmentStateCode\":\"ARONCD\"," +
                "\"whiteDefinitionFlag\":true,\"onlineModes\":null,\"processResourceNames\":null,\"userDataSets\":null," +
                "\"portGroups\":[{\"portGroupName\":\"PG1\",\"accessModes\":[\"Auto\",\"Manual\"],\"capableOperationModes\":null}]," +
                "\"portResources\":[{\"portResourceName\":\"P1\",\"portGroupName\":\"PG1\",\"loadMode\":\"INPUT_OUTPUT\"," +
                "\"associatedPortResourceName\":null,\"loadPurposeTypeId\":\"Any Purpose\",\"loadSequenceNumber\":1,\"unloadSequenceNumber\":1," +
                "\"carrierCategories\":null,\"specialPortControls\":null}],\"equipmentParameters\":null,\"controlLotBanks\":null," +
                "\"operationProcedures\":null,\"fpcCategories\":null,\"whatNextControls\":null,\"specialEquipmentControls\":null," +
                "\"reticlePodPorts\":null,\"whereNextControls\":null,\"resources\":null,\"stockers\":null}}," +
                "\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"2019-04-29 13:51:00\"}";

        String dcSpecJson = "{\"user\":{\"userId\":\"aron\",\"functionId\":\"RELEASE\",\"clientNode\":\"aron\"},\"functionId\":\"RELEASE\"," +
                "\"messageBody\":{\"action\":\"update\",\"releaseType\":\"dataCollectionSpec\",\"body\":{\"dcSpecId\":\"DS_ARON.00\"," +
                "\"description\":\"this is test\",\"fpcCategory\":null,\"whiteDefinitionFlag\":false,\"dcDefinitions\":[\"DC_ARON_CJ.00\"]," +
                "\"dcSpecItems\":[{\"dcItemName\":\"ProcessTime1\",\"screenUpperLimitCheckRequiredFlag\":false,\"screenUpperLimit\":1.0," +
                "\"screenUpperLimitActionIds\":null,\"screenLowerLimitCheckRequiredFlag\":false,\"screenLowerLimit\":1.0," +
                "\"screenLowerLimitActionIds\":null,\"specUpperLimitCheckRequiredFlag\":false,\"specUpperLimit\":1.0," +
                "\"specUpperLimitActionIds\":null,\"specLowerLimitActionIds\":null,\"specLowerLimitCheckRequiredFlag\":false,\"specLowerLimit\":1" +
                ".0,\"controlUpperLimitCheckRequiredFlag\":false,\"controlUpperLimit\":1.0,\"controlUpperLimitActionIds\":null," +
                "\"controlLowerLimitActionIds\":null,\"controlLowerLimitCheckRequiredFlag\":false,\"controlLowerLimit\":1.0,\"targetValue\":1.0," +
                "\"tag\":\"demoData\"}],\"userDataSets\":null}},\"messageId\":\"78d86cac-e314-473e-aed2-69fe487e2a0a\",\"sendTime\":\"2019-05-05 " +
                "13:51:00\"}";

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 1; i++) {
            executorService.execute(() -> {
                String threadName = Thread.currentThread().getName();

                String reply = rabbitTemplate.call("RELEASE-REQUEST", "RELEASE-REQUEST-TEST-001", equipmentJson);
                //rabbitTemplate.ack
                log.info(threadName + " ## reply is :" + reply);

                Response response = JSONObject.parseObject(reply, Response.class);
                if (response.getCode() == Response.SUCCESS) {
                    log.info(">>>>> release success ! <<<<<");
                } else {
                    log.info(">>>>> release failed : {} <<<<<", response.getMessage());
                }
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
        rabbitTemplate.send("MES_ASYNC", "this is another test");
    }
}
