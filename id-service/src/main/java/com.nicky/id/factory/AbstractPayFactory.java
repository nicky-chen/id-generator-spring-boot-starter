/*
 * File Name:AbstractPayFactory is created on 2019/1/2下午2:20 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.factory;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Maps;
import com.nicky.annotation.IdStrategy;
import com.nicky.constants.IdFactoryEnum;

/**
 * @author nicky_chin
 * @Description: 抽象工厂类
 * @date: 2019/1/2 下午2:20
 * @since JDK 1.8
 */
public abstract class AbstractPayFactory implements IdFactory, ApplicationContextAware {

    protected Map<Class, Map<? extends Enum, Object>> data = Maps.newHashMap();

    /**
     * 获取bean
     *
     * @param applicationContext
     *
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Arrays.stream(IdFactoryEnum.values()).forEach(clazz -> {
            Map<String, Object> beans = applicationContext.getBeansOfType(clazz.getClazz());
            Map<Enum<? extends Enum>, Object> enumMap = Maps.newHashMap();
            beans.forEach((k, v) -> {
                IdStrategy annotation = v.getClass().getAnnotation(IdStrategy.class);
                if (annotation != null && annotation.isEnable()) {
                    Enum key = Enum.valueOf(annotation.enumType(), annotation.enumName());
                    enumMap.put(key, v);
                }
            });
            data.put(clazz.getClazz(), enumMap);
        });
    }

    /**
     * 获取策略数据
     *
     * @param clazz
     * @param enumType
     * @param <T>
     *
     * @return
     */
    protected <T> T getStrategyByClass(Class<? extends T> clazz, Enum enumType) {
        return (T)data.get(clazz).get(enumType);
    }

}
