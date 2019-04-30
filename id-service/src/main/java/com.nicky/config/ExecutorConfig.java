/*
 * File Name:ExecutorConfig is created on 2018/11/15下午4:35 by nicky_chen
 *
 * Copyright (c) 2018, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.vip.vjtools.vjkit.concurrent.threadpool.AbortPolicyWithReport;

/**
 * @author nicky_chin
 * @Description: 线程池配置类
 * @date: 2018/11/15 下午4:35
 * @since JDK 1.8
 */
@Configuration
public class ExecutorConfig {

    // 预计注册并发不会太高，缓冲队列给100
    private static final Integer SIGN_UP_EXECUTOR_QUEUE_SIZE = 100;

    /**
     * 用户注册异步处理线程池，如新建账户，ext，推送等
     * 由于保证必须执行，这里线程池的拒绝策略设为由调度线程执行
     *
     * @return
     */
    @Bean(name = "eventExecutor")
    public Executor getSignUpExecutor() {
        return new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(SIGN_UP_EXECUTOR_QUEUE_SIZE),
            new ThreadFactoryBuilder().setNameFormat("SignUpExecutor-[%d]").build(),
            // 这里队列满时，由调用线程执行
            new AbortPolicyWithReport("sfsdf"));
    }
}
