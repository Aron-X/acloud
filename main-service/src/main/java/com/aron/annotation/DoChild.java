package com.aron.annotation;

import com.aron.entity.BaseEntity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description:
 * <p>DoProperty .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/2/28        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/2/28 14:11
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DoChild {

    TYPE type() default TYPE.SINGLE;

    Class<? extends BaseEntity> child();

    boolean lazy() default true;

    enum TYPE {
        /**
         * single entity
         */
        SINGLE,
        /**
         * list
         */
        LIST;
    }
}
