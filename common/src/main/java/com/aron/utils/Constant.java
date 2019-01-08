package com.aron.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * description:
 * <p>Constant .<br/></p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/9/21        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/9/21 13:28
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
public class Constant {
    private Constant() {

    }

    public final static String STRATEGY_REFERENCE = "com.aron.generator.SnowflakeIDWorker";

    public static <M> Long getId(M target) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(target.getClass());
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                if (null != descriptor.getReadMethod() && "id".equals(descriptor.getName())) {
                    Object originalValue = descriptor.getReadMethod()
                            .invoke(target);
                    return (Long) originalValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
