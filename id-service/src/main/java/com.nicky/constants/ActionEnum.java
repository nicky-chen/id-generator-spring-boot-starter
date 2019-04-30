/*
 * File Name:EventPulisher is created on 2018/12/13下午5:34 by nicky_chen
 *
 * Copyright (c) 2018, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author nicky_chin
 * @Description: 责任链规则
 * @date: 2018/12/13 下午5:34
 * @since JDK 1.8
 */
@Getter
@AllArgsConstructor
public enum ActionEnum {

    /**
     *
     */
    ONE("ONE"),
    TWO("TWO");

    private String msgType;
}
