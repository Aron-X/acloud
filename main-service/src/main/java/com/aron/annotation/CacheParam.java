package com.aron.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description:
 * <p>CacheParam .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/1/10        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/1/10 16:17
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Documented
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CacheParam {
    String name();
}
