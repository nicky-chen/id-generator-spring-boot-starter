/*
 * File Name:LoadLevel is created on 2019/4/12下午7:00 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 * 
 */
package com.nicky.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 
 *
 * @author nicky_chin
 * @date: 2019/4/12 下午7:00
 *
 * @since JDK 1.8
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LoadLevel {
    /**
     * Name string.
     *
     * @return the string
     */
    String name();

    /**
     * Order int.
     *
     * @return the int
     */
    int order();
}
