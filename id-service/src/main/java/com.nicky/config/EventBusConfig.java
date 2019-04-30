/*
 * File Name:EventBusConfig is created on 2018/12/13下午4:51 by nicky_chen
 *
 * Copyright (c) 2018, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

/**
 * @author nicky_chin
 * @Description:
 * @date: 2018/12/13 下午4:51
 * @since JDK 1.8
 */
@Configuration
public class EventBusConfig {

    @Bean(name = "syncBus")
    public EventBus eventBus() {
        EventBus eventBus = new EventBus();
        return eventBus;
    }

    @Bean(name = "asyncBus")
    public EventBus asycBus(@Qualifier(value = "eventExecutor") Executor executor) {
        AsyncEventBus asyncEventBus = new AsyncEventBus(executor);
        return asyncEventBus;

    }
}
