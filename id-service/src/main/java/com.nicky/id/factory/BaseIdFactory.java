/*
 * File Name:BasePayFactory is created on 2019/1/2下午2:16 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.factory;

import org.springframework.stereotype.Component;

import com.nicky.constants.IdFailType;
import com.nicky.constants.IdGeneratorType;
import com.nicky.spi.IdFailStrategy;
import com.nicky.spi.IdSender;

/**
 * @author nicky_chin
 * @Description: 基础工厂类
 * @date: 2019/1/2 下午2:16
 * @since JDK 1.8
 */
@Component("basePayFactory")
public class BaseIdFactory extends AbstractPayFactory {
    @Override
    public IdSender getIdGeneratorStrategy(IdGeneratorType type) {
        return getStrategyByClass(IdSender.class, type);
    }

    @Override
    public IdFailStrategy getIdFailStrategy(IdFailType type) {
        return getStrategyByClass(IdFailStrategy.class, type);
    }
}
