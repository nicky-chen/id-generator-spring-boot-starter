/*
 * File Name:GenerateInterface is created on 2019/3/27上午10:43 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 * 
 */
package com.nicky.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 
 *
 * @author nicky_chin
 * @date: 2019/3/27 上午10:43
 *
 * @since JDK 1.8
 */
@Target(ElementType.TYPE)//注解使用目标为类
@Retention(RetentionPolicy.SOURCE)//注解保留范围为源代码
public @interface GenerateInterface {
    String suffix() default "Interface";//生成对应接口的后缀名
}
