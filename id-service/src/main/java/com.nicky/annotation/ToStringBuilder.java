/*
 * File Name:ToStringBuilder is created on 2019/4/21上午11:36 by nicky_chen
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


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ToStringBuilder {
}
