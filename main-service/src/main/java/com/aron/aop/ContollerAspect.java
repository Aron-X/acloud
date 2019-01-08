package com.aron.aop;

import com.aron.exception.ProcessFailedException;
import com.aron.utils.ThreadContextHolder;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 * This ContollerAspect used to handle global exceptions.
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/6/28        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/6/28 11:10
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
@Slf4j
@ControllerAdvice
public class ContollerAspect {

    /**
     * ProcessFailedException异常捕捉处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ProcessFailedException.class)
    public Map ProcessFailedHandler(ProcessFailedException e) {
        log.info("ProcessFailedHandler():");
        String txId = ThreadContextHolder.getTransactionId();
        Map<String, String> map = new HashMap<>();
        map.put("txIdFirst", txId);
        return map;
    }

    /**
     * 全局异常捕捉处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception e) {
        log.info("errorHandler():" + e.getMessage());
        String txId = ThreadContextHolder.getTransactionId();
        Map<String, String> map = new HashMap<>();
        map.put("txIdSecond", txId);
        return map;
    }

}
