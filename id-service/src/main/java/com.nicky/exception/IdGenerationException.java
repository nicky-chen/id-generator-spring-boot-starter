/*
 * File Name:IdGenerationException is created on 2019/4/7下午8:41 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 * 
 */
package com.nicky.exception;

import java.util.function.Supplier;

/**
 * @description: 
 *
 * @author nicky_chin
 * @date: 2019/4/7 下午8:41
 *
 * @since JDK 1.8
 */
public class IdGenerationException extends RuntimeException implements Supplier<RuntimeException>, Cloneable {
    @Override
    public RuntimeException get() {
        return null;
    }
}
