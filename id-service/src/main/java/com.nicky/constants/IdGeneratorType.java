/*
 * File Name:IdGeneratorType is created on 2019/3/22下午4:34 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.constants;

import com.nicky.annotation.AnnotationFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@AnnotationFormat
public enum IdGeneratorType {
    UNDEFINED,
    UUID,
    SIMPLE_SNOW_FLAKE,
    ZOOKEEPER,
    REDIS,
    ;

}
