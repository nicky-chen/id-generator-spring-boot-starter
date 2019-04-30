/*
 * File Name:StarterService is created on 2019/3/22上午11:29 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.service;

import org.springframework.util.StringUtils;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/22 上午11:29
 * @since JDK 1.8
 */
public class StarterService {

    private String config;

    public StarterService(String config) {
        this.config = config;
    }

    public String[] split(String separatorChar) {
        return StringUtils.split(this.config, separatorChar);
    }

}
