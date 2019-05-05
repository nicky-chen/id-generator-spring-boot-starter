/*
 * File Name:StringStyle is created on 2019/5/5上午9:32 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.lombok;

/**
 * @author nicky_chin
 * @description: 类型
 * @date: 2019/5/5 上午9:32
 * @since JDK 1.8
 */

public enum StringStyle {

    DEFAULT_STYLE("org.apache.commons.lang3.builder.ToStringStyle.DEFAULT_STYLE"),

    MULTI_LINE_STYLE("org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE"),

    NO_FIELD_NAMES_STYLE("org.apache.commons.lang3.builder.ToStringStyle.NO_FIELD_NAMES_STYLE"),

    SHORT_PREFIX_STYLE("org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE"),

    SIMPLE_STYLE("org.apache.commons.lang3.builder.ToStringStyle.SIMPLE_STYLE"),

    NO_CLASS_NAME_STYLE("org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE"),

    JSON_STYLE("org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE"),

    ;

    public String getToStringStyle() {
        return toStringStyle;
    }

    private String toStringStyle;

    StringStyle(String toStringStyle) {
        this.toStringStyle = toStringStyle;
    }

}
