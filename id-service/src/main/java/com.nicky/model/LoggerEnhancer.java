/*
 * File Name:LoggerAdapter is created on 2019/4/20上午10:50 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.model;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vip.vjtools.vjkit.logging.PerformanceUtil;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/4/20 上午10:50
 * @since JDK 1.8
 */
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "warp")
public class LoggerEnhancer implements Logger {

    @Delegate
    @NonNull
    private Logger logger;

    public static LoggerEnhancer getLogger(Class<?> clazz) {
       return warp(LoggerFactory.getLogger(clazz));
    }

    public void info(final Supplier<String> messageSupplier) {
        if (isInfoEnabled()) {
            logger.info(messageSupplier.get());
        }
    }

    public void error(final Supplier<String> messageSupplier) {
        if (isErrorEnabled()) {
            error(messageSupplier.get());
        }
    }

    /**
     * Logs a message at ERROR level.
     */
    public void error(final Supplier<String> messageSupplier, final Throwable throwable) {
        if (isErrorEnabled()) {
            error(messageSupplier.get(), throwable);
        }
    }

    /**
     * 记录结束时间并当处理时间超过预定的阈值时发出警告信息，最后清除
     * @param threshold 阈值（单位：ms）
     */
    public void endWithSlowLog(long threshold) {
        PerformanceUtil.start();
        PerformanceUtil.endWithSlowLog(logger, threshold);
    }

}
