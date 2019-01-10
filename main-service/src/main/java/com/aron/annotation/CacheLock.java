package com.aron.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * description:
 * <p>CacheLock .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/1/10        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/1/10 15:29
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CacheLock {

    /**
     * @return key
     */
    String key();

    /**
     * @return expire
     */
    int expire() default 60;

    /**
     * @return TimeUnit
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    String delimiter() default ":";
}
