package com.aron.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description:
 * <p><br/></p>
 * change history:
 * date             defect             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 *
 * @author Ho
 * @date 2018/10/11 16:42:02
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IdentifierColumn {

    String value();

}
