package com.aron.service;


import com.aron.annotation.DoChild;
import com.aron.annotation.DoMapping;
import com.aron.annotation.DoProperty;
import com.aron.entity.BaseEntity;
import com.aron.utils.Changeable;
import com.aron.utils.ObjectIdentifier;
import com.aron.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * description:
 * <p>BoFactory .</p>
 * <p>
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2019/2/28        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2019/2/28 14:17
 * @copyright: 2019, FA Software (Chengdu) Co., Ltd. All Rights Reserved.
 */
@Slf4j
@Component
public class BoFactory {

    @PersistenceContext
    private EntityManager entityManager;

    public <T extends BaseBO> T convertObjectIdentifierToBO(ObjectIdentifier objectIdentifier, Class<T> clazzType) {
        T bo = null;
        try {
            BaseEntity entity = entityManager.find(getDoMappingType(clazzType), objectIdentifier.getReferenceKey());

            bo = SpringContextUtil.getBean(clazzType, entity);
            //TODO check objectIdentifier value
            putDoDataToBo(entity, bo);
        } catch (Exception e) {
            log.error("create BO failed !", e);
        }
        return bo;
    }

    private Class<? extends BaseEntity> getDoMappingType(Class<? extends BaseBO> boClazz) {
        String[] beanNamesForType = SpringContextUtil.getApplicationContext().getBeanNamesForType(boClazz);
        if (beanNamesForType == null || beanNamesForType.length != 1) {
            throw new RuntimeException("bo type is invalid or there are more than one impl for one BO");
        }
        DoMapping doMapping =
                Objects.requireNonNull(SpringContextUtil.getApplicationContext().getType(beanNamesForType[0])).getAnnotation(DoMapping.class);
        if (doMapping == null) {
            return null;
        }
        return doMapping.clazz();
    }

    private void putDoDataToBo(BaseEntity entity, BaseBO baseBO) throws Exception {
        List<Field> fields = new ArrayList<>();
        ReflectionUtils.doWithFields(baseBO.getClass(), field -> {
            DoProperty doProperty = field.getAnnotation(DoProperty.class);
            DoChild doChild = field.getAnnotation(DoChild.class);
            if (doProperty != null || doChild != null) {
                fields.add(field);
            }
        });
        for (Field field : fields) {
            DoProperty doProperty = field.getAnnotation(DoProperty.class);
            if (doProperty != null) {
                processDoProperty(doProperty, entity, baseBO, field);
            }
            DoChild doChild = field.getAnnotation(DoChild.class);
            if (doChild != null) {
                processDoChild(doChild, entity.getId(), baseBO, field);
            }
        }
    }

    private void processDoChild(DoChild doChild, String primaryKey, BaseBO baseBO, Field field) throws Exception {
        if (!List.class.isAssignableFrom(field.getType()) && field.getType() != Changeable.class) {
            log.error("child property must be a List or Changeable type !");
            return;
        }
        boolean isLazyLoad = doChild.lazy();
        //if lazy is true, then skip
        if (isLazyLoad) {
            return;
        }
        Class<?> child = doChild.child();
        Table table = child.getAnnotation(Table.class);
        Query nativeQuery = entityManager.createNativeQuery("SELECT * FROM " + table.name() + " where refkey = ?", child);
        List<?> resultList = nativeQuery.setParameter(1, primaryKey).getResultList();
        //set data
        Method boWriteMethod = ReflectionUtils.findMethod(baseBO.getClass(), writeMethodName(field.getName()), field.getType());
        //check field type
        DoChild.TYPE fieldType = doChild.type();
        Object result = null;
        if (fieldType == DoChild.TYPE.LIST) {
            result = new ArrayList<Changeable>();
            for (Object item : resultList) {
                ((List<Changeable>) result).add(Changeable.of(item));
            }
        } else {
            result = Changeable.of(resultList == null ? null : resultList.get(0));
        }
        Assert.notNull(boWriteMethod, "boWriteMethod is null");
        ReflectionUtils.makeAccessible(boWriteMethod);
        ReflectionUtils.invokeMethod(boWriteMethod, baseBO, result);
        //boWriteMethod.invoke(baseBO, result);
    }

    private void processDoProperty(DoProperty doProperty, BaseEntity entity, BaseBO baseBO, Field field) throws Exception {
        String[] attributes = doProperty.attributes();
        if (attributes.length == 1) {
            copyEntityValueToTarget(attributes[0], entity, baseBO, field);
            return;
        }
        if (attributes.length > 1) {
            String[] mapping = doProperty.mapping();
            Class<?> fieldClazz = field.getType();
            Object fieldInstance = fieldClazz.newInstance();
            for (int i = 0; i < attributes.length; i++) {
                copyEntityValueToTarget(attributes[i], entity, fieldInstance, fieldClazz.getDeclaredField(mapping[i]));
            }
            Method boWriteMethod = ReflectionUtils.findMethod(baseBO.getClass(), writeMethodName(field.getName()), field.getType());
            Assert.notNull(boWriteMethod, "boWriteMethod is null");
            ReflectionUtils.makeAccessible(boWriteMethod);
            boWriteMethod.invoke(baseBO, fieldInstance);

        }
    }

    private void copyEntityValueToTarget(String entityAttribute, BaseEntity entity,
                                         Object writeObj, Field writeField) throws Exception {
        Method entityGetMethod = ReflectionUtils.findMethod(entity.getClass(), readMethodName(entityAttribute));
        Object entityAttrValue = entityGetMethod.invoke(entity);

        Method boWriteMethod = ReflectionUtils.findMethod(writeObj.getClass(), writeMethodName(writeField.getName()), writeField.getType());
        Assert.notNull(boWriteMethod, "boWriteMethod is null");
        ReflectionUtils.makeAccessible(boWriteMethod);
        boWriteMethod.invoke(writeObj, entityAttrValue);
    }

    private String readMethodName(String propertyName) {
        Assert.hasLength(propertyName, "propertyName is empty");
        return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    private String writeMethodName(String propertyName) {
        Assert.hasLength(propertyName, "propertyName is empty");
        return "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

}
