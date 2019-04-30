/*
 * File Name:EnableIdGenerator is created on 2019/3/22下午5:57 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.nicky.config.IdGeneratorRegistrar;
import com.nicky.constants.IdGeneratorType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(IdGeneratorRegistrar.class)
@SpringBootApplication
public @interface EnableIdGeneratorConfig {

    /**
     * id生成策略
     * @see com.nicky.IdGeneratorContext
     * @return
     */
    IdStrategy[] dStrategy() default {@IdStrategy(enumType = IdGeneratorType.class, enumName = "UUID")};

}
