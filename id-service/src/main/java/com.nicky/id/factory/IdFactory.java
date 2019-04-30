/*
 * File Name:PayFactory is created on 2019/1/2下午2:15 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.factory;

import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.MethodUtils;

import com.nicky.constants.IdFactoryEnum;
import com.nicky.constants.IdFailType;
import com.nicky.constants.IdGeneratorType;
import com.nicky.spi.IdFailStrategy;
import com.nicky.spi.IdSender;

import jodd.log.Logger;
import jodd.log.LoggerFactory;

/**
 * @author nicky_chin
 * @Description: 工厂方法接口
 * @date: 2019/1/2 下午2:15
 * @since JDK 1.8
 */
public interface IdFactory {

    Logger LOGGER = LoggerFactory.getLogger(IdFactory.class);

    /**
     * @param type
     *
     * @return
     */
    IdSender getIdGeneratorStrategy(IdGeneratorType type);

    /**
     * @param type
     *
     * @return
     */
    IdFailStrategy getIdFailStrategy(IdFailType type);

    /**
     * 通用获取策略方法
     *
     * @param factory  方法类
     * @param enumType 方法参数
     * @param <T>
     *
     * @return
     */
    default <T> T getStrategy(IdFactoryEnum factory, Enum<? extends Enum> enumType) {

        Method method = MethodUtils
            .getMatchingAccessibleMethod(this.getClass(), factory.getMethodName(), new Class[] {enumType.getClass()});
        try {
            Object invoke = method.invoke(this, enumType);
            return (T)invoke;
        } catch (ReflectiveOperationException e) {
            LOGGER.error(e::getLocalizedMessage, e);
        }
        return null;
    }
}
