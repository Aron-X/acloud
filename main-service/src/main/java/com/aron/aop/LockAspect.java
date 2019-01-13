package com.aron.aop;

import com.aron.annotation.CacheLock;
import com.aron.lock.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

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
public class LockAspect {

    private final RedisLock lock;

    @Autowired
    public LockAspect(RedisLock lockHandler) {
        this.lock = lockHandler;
    }

    @Pointcut("execution(public * *(..)) && @annotation(com.aron.annotation.CacheLock)")
    public void lockPoint() {
    }

    @Around("lockPoint()")
    public Object lockProcess(ProceedingJoinPoint joinPoint) {
        doBefore(joinPoint);
        log.info("lockProcess start....");
        Object result = null;
        try {
            if (lock.lock()) {
                result = joinPoint.proceed();
            } else {
                throw new RuntimeException("retrieve lock failed");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            // 为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，
            // 操作完的时候锁因为超时已经被别人获得，这时就不必解锁了.
            if (!lock.isLockExpired()) {
                lock.unlock();
            }
        }
        return result;
    }

    private void doBefore(ProceedingJoinPoint joinPoint) {
        log.info("init key start....");
        String lockKey = getLockKey(joinPoint);
        lock.setLockKey(lockKey);
    }

    private String getLockKey(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lockAnnotation = method.getAnnotation(CacheLock.class);
        return String.format("%s#%s%s%s", lockAnnotation.key(), method.getDeclaringClass().getSimpleName(), lockAnnotation.delimiter(), method.getName());
    }
}
