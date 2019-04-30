/*
 * File Name:PropertyEnviroment is created on 2019/3/22下午2:06 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 * 
 */
package com.nicky.config;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @description: 
 *
 * @author nicky_chin
 * @date: 2019/3/22 下午2:06
 *
 * @since JDK 1.8
 */
public class PropertyEnvironment implements EnvironmentAware {

    protected RelaxedPropertyResolver readResolver;

    protected RelaxedPropertyResolver mybatisResolver;

    /*
     * (non-Javadoc)
     * @see org.springframework.context.EnvironmentAware#setEnvironment(org.springframework.core.env.Environment)
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.readResolver = new RelaxedPropertyResolver(environment, "spring.datasource.read.");
        this.mybatisResolver = new RelaxedPropertyResolver(environment, "mybatis.");
    }
}
