package com.aron.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * description:
 * <p>SpringContextUtil .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/3/4        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/3/4 13:17
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext pApplicationContext) throws BeansException {
        System.out.println("------------------------set context---------------------");
        SpringContextUtil.applicationContext = pApplicationContext;
    }

    /**
     * getApplicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * getBean
     *
     * @param name
     * @return bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * getBean
     *
     * @param clazz
     * @param <T>
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * getBean
     *
     * @param clazz
     * @param args
     * @param <T>
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz, Object... args) {
        return getApplicationContext().getBean(clazz, args);
    }

    /**
     * getBean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
