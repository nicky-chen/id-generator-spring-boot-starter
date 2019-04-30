/*
 * File Name:PayFactoryEnum is created on 2019/1/2下午3:30 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author nicky_chin
 * @Description: 策略枚举类
 * @date: 2019/1/2 下午3:30
 * @since JDK 1.8
 */
@Getter
@AllArgsConstructor
public enum IdFactoryEnum {


    ;

    private Class clazz;

    /**
     * 工厂策略具体方法
     */
    private String methodName;
}
