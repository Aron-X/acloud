package com.aron;

import com.alibaba.fastjson.JSONObject;
import com.aron.dto.Response;
import com.aron.rabbitmq.CimRabbitTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    private CimRabbitTemplate rabbitTemplate;

    @Value("classpath:json/bank.json")
    private Resource bank;

    @Value("classpath:json/dc_def.json")
    private Resource dcDef;

    @Value("classpath:json/dc_spec.json")
    private Resource dcSpec;

    @Value("classpath:json/equipment.json")
    private Resource equipment;

    @Value("classpath:json/equipment_state.json")
    private Resource equipmentState;

    @Value("classpath:json/logical_recipe.json")
    private Resource logicalRecipe;

    @Value("classpath:json/mfg_layer.json")
    private Resource mfgLayer;

    @Value("classpath:json/pd.json")
    private Resource pd;

    @Value("classpath:json/recipe.json")
    private Resource recipe;

    @Value("classpath:json/user.json")
    private Resource user;

    @Value("classpath:json/product_spec.json")
    private Resource productSpeJson;

    @Value("classpath:json/product_group.json")
    private Resource productGroup;

    @Value("classpath:json/user_group.json")
    private Resource userGroup;

    @Value("classpath:json/privilege_group.json")
    private Resource privilegeGroup;

    @Value("classpath:json/reticle_set.json")
    private Resource reticleSet;

    @Value("classpath:json/reticle.json")
    private Resource reticle;

    @Value("classpath:json/reticle_pod.json")
    private Resource reticlePod;

    @Value("classpath:json/cassette.json")
    private Resource cassette;

    @Value("classpath:json/stocker.json")
    private Resource stocker;


    @Test
    public void test() {
        log.info(">>> start <<<");
        String jsonValue = jsonReader(stocker);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 1; i++) {
            executorService.execute(() -> {
                String threadName = Thread.currentThread().getName();

                String reply = rabbitTemplate.call("DEPLOY-REQUEST", "DEPLOY-REPLY-TEST-001", jsonValue);
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

    private String jsonReader(Resource resource) {
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader(resource.getFile()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    @Test
    public void test1() {
        rabbitTemplate.send("MES_ASYNC", "this is another test");
    }

}
