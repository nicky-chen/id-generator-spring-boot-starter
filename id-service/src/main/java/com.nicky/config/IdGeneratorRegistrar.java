/*
 * File Name:IdGeneratorAutoConfigure is created on 2019/3/22下午5:59 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.nicky.annotation.EnableIdGeneratorConfig;
import com.nicky.util.BeanRegistrationUtil;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/22 下午5:59
 * @since JDK 1.8
 */
public class IdGeneratorRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
            .getAnnotationAttributes(EnableIdGeneratorConfig.class.getName()));
        //todo nicky

        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, IdGeneratorRegistrar.class.getName(),
            IdGeneratorRegistrar.class);

    }
}
