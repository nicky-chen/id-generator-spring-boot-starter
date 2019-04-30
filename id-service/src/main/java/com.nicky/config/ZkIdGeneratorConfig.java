/*
 * File Name:ZkIdGeneratorConfig is created on 2019/4/7上午11:11 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 * 
 */
package com.nicky.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: 
 *
 * @author nicky_chin
 * @date: 2019/4/7 上午11:11
 *
 * @since JDK 1.8
 */
@Getter
@Setter
@ConfigurationProperties("id.zookeeper")
public class ZkIdGeneratorConfig {

    private String namespace;

    private String connectUrls;

    private String workPath;
}
