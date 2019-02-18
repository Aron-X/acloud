package com.aron.aop;

import com.aron.annotation.MethodLock;
import com.aron.lock.OmZookeeperLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;

/**
 * description:
 * <p>LockAspect .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/1/10        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/1/10 14:37
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Aspect
@Configuration
@Slf4j
public class MethodLockAspect {

//    private final RedisLock lock;

    private OmZookeeperLock lockRegister;

    @Autowired
    public MethodLockAspect(OmZookeeperLock lockRegister) {
        this.lockRegister = lockRegister;
    }

    @Pointcut("execution(public * *(..)) && @annotation(com.aron.annotation.MethodLock)")
    public void lockPoint() {
    }

    @Around("lockPoint()")
    public Object lockProcess(ProceedingJoinPoint joinPoint) {
        doBefore(joinPoint);
        log.debug("lockProcess start....");
        Object result = null;
        String lockKey = getLockKey(joinPoint);
        Lock lock = lockRegister.obtain(lockKey);
        boolean acquired = true;
        try {
            acquired = lock.tryLock();
            //如果没有获取到所，则抛异常返回
            if (!acquired) {
                throw new RuntimeException("Please tries later");
            }
            result = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }
        return result;
    }

    private void doBefore(ProceedingJoinPoint joinPoint) {
        log.debug("doBefore start....");
    }

    private String getLockKey(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        MethodLock lockAnnotation = method.getAnnotation(MethodLock.class);
        return String.format("%s_%s%s%s", lockAnnotation.key(), method.getDeclaringClass().getSimpleName(), lockAnnotation.delimiter(),
                method.getName());
    }
}
