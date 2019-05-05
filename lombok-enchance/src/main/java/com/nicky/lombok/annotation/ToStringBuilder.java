/*
 * File Name:ToStringBuilder is created on 2019/4/21上午11:36 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.lombok.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nicky.lombok.StringStyle;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface ToStringBuilder {

    /**
     * 返回toString格式
     * @return
     */
    StringStyle toStringStyle() default StringStyle.JSON_STYLE;
}
