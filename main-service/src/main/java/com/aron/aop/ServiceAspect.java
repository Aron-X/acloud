package com.aron.aop;

import com.aron.bo.BaseBO;
import com.aron.bo.BizThreadHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * description:
 * <p>ServiceAspect .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/3/7        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/3/7 10:10
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Aspect
@Configuration
@Slf4j
public class ServiceAspect {
    @Pointcut("execution(public * com.aron.service..*.*(..)) || @annotation(com.aron.annotation.BizCaller)")
    public void servicePoint() {
    }

    @Around("servicePoint()")
    public Object serviceProcess(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("AOP process failed", throwable);
        }
        List<BaseBO> boList = BizThreadHolder.getBizList();
        if (boList == null) {
            return result;
        }
        boList.forEach(boItem -> {
            //invoke pushChanges for BO
            Method pushChanges = ReflectionUtils.findMethod(boItem.getClass(), "pushChanges");
            ReflectionUtils.makeAccessible(Objects.requireNonNull(pushChanges));
            ReflectionUtils.invokeMethod(pushChanges, boItem);
        });
        //clear invoked pushChanges bo
        BizThreadHolder.clearHolder();
        return result;
    }
}
