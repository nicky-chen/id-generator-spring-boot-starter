/*
 * File Name:PayStrategy is created on 2019/1/2下午4:33 by nicky_chen
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
 * @author nicky_chin
 * @date: 2019/1/2 下午4:33
 * @since JDK 1.8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdStrategy {

    /**
     * 枚举class
     *
     * @return
     */
    Class<? extends Enum> enumType();

    /**
     * 具体枚举
     *
     * @return
     */
    String enumName();

    /**
     * 策略是否有效
     *
     * @return
     */
    boolean isEnable() default true;
}
