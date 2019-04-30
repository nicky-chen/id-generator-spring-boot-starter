/*
 * File Name:BeanRegistrationUtil is created on 2019/3/22下午2:28 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.util;

import java.util.Objects;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/22 下午2:28
 * @since JDK 1.8
 */
public class BeanRegistrationUtil {

    public static boolean registerBeanDefinitionIfNotExists(BeanDefinitionRegistry registry, String beanName,
        Class<?> beanClass) {
        if (registry.containsBeanDefinition(beanName)) {
            return false;
        }

        String[] candidates = registry.getBeanDefinitionNames();

        for (String candidate : candidates) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(candidate);
            if (Objects.equals(beanDefinition.getBeanClassName(), beanClass.getName())) {
                return false;
            }
        }

        BeanDefinition annotationProcessor = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
        registry.registerBeanDefinition(beanName, annotationProcessor);

        return true;
    }
}
