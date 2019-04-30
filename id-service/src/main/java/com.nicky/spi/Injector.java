/*
 * File Name:Injector is created on 2019/3/22下午7:06 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 * 
 */
package com.nicky.spi;

/**
 * @description: 
 *
 * @author nicky_chin
 * @date: 2019/3/22 下午7:06
 *
 * @since JDK 1.8
 */
public interface Injector {

    /**
     * Returns the appropriate instance for the given injection type
     */
    <T> T getInstance(Class<T> clazz);

    /**
     * Returns the appropriate instance for the given injection type and name
     */
    <T> T getInstance(Class<T> clazz, String name);
}
