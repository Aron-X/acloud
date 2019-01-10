package com.aron.aop;

import com.aron.annotation.CacheLock;
import com.aron.annotation.CacheParam;
import com.aron.lock.LockHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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

    private final LockHandler lock;

    @Autowired
    public LockAspect(LockHandler lockHandler) {
        this.lock = lockHandler;
    }

    @Around("execution(public * *(..)) && @annotation(com.aron.annotation.CacheLock)")
    public Object lockProcess(ProceedingJoinPoint joinPoint) {
        /*MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheLock cacheLockAnnotation = method.getAnnotation(CacheLock.class);
        Assert.checkNonNull(cacheLockAnnotation);
        if (StringUtils.isEmpty(cacheLockAnnotation.key())) {
            throw new RuntimeException("lock key can not be empty");
        }*/
        log.info("lockProcess start....");
        String lockKey = getLockKey(joinPoint);
        lock.setLockKey(lockKey);
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
            String timeOutValueStr = lock.get(lockKey);
            boolean isExpire = timeOutValueStr != null && Long.parseLong(timeOutValueStr) < System.currentTimeMillis();
            if (!isExpire) {
                lock.unlock();
            }
        }
        return result;
    }

    public String getLockKey(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lockAnnotation = method.getAnnotation(CacheLock.class);
        final Object[] args = pjp.getArgs();
        final Parameter[] parameters = method.getParameters();
        StringBuilder builder = new StringBuilder(lockAnnotation.delimiter() + method.getName());
        //默认解析方法里面带 CacheParam 注解的属性,如果没有尝试着解析实体对象中的
        for (int i = 0; i < parameters.length; i++) {
            final CacheParam annotation = parameters[i].getAnnotation(CacheParam.class);
            if (annotation == null) {
                continue;
            }
            builder.append(lockAnnotation.delimiter()).append(args[i]);
        }
        if (StringUtils.isEmpty(builder.toString())) {
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                final Object object = args[i];
                final Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    final CacheParam annotation = field.getAnnotation(CacheParam.class);
                    if (annotation == null) {
                        continue;
                    }
                    field.setAccessible(true);
                    builder.append(lockAnnotation.delimiter()).append(ReflectionUtils.getField(field, object));
                }
            }
        }
        return lockAnnotation.key() + builder.toString();
    }
}
