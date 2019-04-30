/*
 * File Name:StarterServiceProperties is created on 2019/3/22上午11:30 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/22 上午11:30
 * @since JDK 1.8
 */
@ConfigurationProperties("default.string")
public class StarterServiceProperties {
    private String config;

    public void setConfig(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }
}