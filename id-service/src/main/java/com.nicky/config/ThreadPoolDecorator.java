/*
 * File Name:ThreadPoolDecorator is created on 2019/5/30上午10:17 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Stopwatch;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.nicky.model.LoggerEnhancer;

import jodd.util.StringPool;
import lombok.Getter;

/**
 * @author nicky_chin
 * @description: 线程池装饰器监控类
 * @date: 2019/5/30 上午10:17
 * @since JDK 1.8
 */
@Getter
public class ThreadPoolDecorator extends ThreadPoolExecutor {

    private static final LoggerEnhancer LOGGER = LoggerEnhancer.getLogger(ThreadPoolDecorator.class);

    private ThreadPoolExecutor executor;

    private boolean waitForTasksToCompleteOnShutdown;

    private String poolName;

    private ConcurrentHashMap<String, Stopwatch> timer;

    private ThreadPoolDecorator(ThreadPoolExecutor executor, long awaitTerminationSeconds) {
        this(executor.getCorePoolSize(), executor.getMaximumPoolSize(), executor.getKeepAliveTime(TimeUnit.SECONDS),
            TimeUnit.SECONDS, executor.getQueue(), executor.getThreadFactory());
        this.executor = executor;
        int expectedSize = executor.getMaximumPoolSize();
        if (expectedSize < 3) {
            expectedSize = expectedSize + 1;
        }
        if (expectedSize < Ints.MAX_POWER_OF_TWO) {
            expectedSize = (int)((float)expectedSize / 0.75F + 1.0F);
        }
        this.timer = new ConcurrentHashMap<>(expectedSize);
        MoreExecutors.addDelayedShutdownHook(executor, awaitTerminationSeconds, TimeUnit.SECONDS);
    }

    private ThreadPoolDecorator(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public static ThreadPoolDecorator warp(ThreadPoolExecutor executor, long awaitTerminationSeconds) {
        return new ThreadPoolDecorator(executor, awaitTerminationSeconds);
    }

    public ThreadPoolDecorator poolName(String poolName) {
        this.poolName = StringUtils.defaultString(poolName, StringPool.EMPTY);
        return this;
    }

    public ThreadPoolDecorator waitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
        return this;
    }

    public ListeningExecutorService toListeningExecutor() {
        return MoreExecutors.listeningDecorator(this);
    }

    /**
     * 任务执行之前,记录任务开始时间
     */
    @Override
    public void beforeExecute(Thread t, Runnable r) {
        // 统计已执行任务、正在执行任务、未执行任务数量
        LOGGER.info(String
            .format("threadPool [%s] Going to execute. Executed tasks: %d, Running tasks: %d, Pending tasks: %d",
                poolName, this.getCompletedTaskCount(), this.getActiveCount(), this.getQueue().size()));
        timer.put(String.valueOf(r.hashCode()), Stopwatch.createStarted());
    }

    /**
     * 任务执行之后,计算任务结束时间
     */
    @Override
    public void afterExecute(Runnable r, Throwable t) {
        Stopwatch stopwatch = timer.remove(String.valueOf(r.hashCode()));
        // 统计任务耗时、初始线程数、核心线程数、正在执行的任务数量、已完成任务数量、任务总数、队列里缓存的任务数量、池中存在的最大线程数、最大允许的线程数、线程空闲时间、线程池是否关闭、线程池是否终止
        LOGGER.info(String.format(this.poolName
                + "-pool-monitor: Duration: %d ms, PoolSize: %d, CorePoolSize: %d, Active: %d, Completed: %d, Task: %d, Queue: %d, LargestPoolSize: %d, MaximumPoolSize: %d,KeepAliveTime: %d, isShutdown: %s, isTerminated: %s",
            stopwatch.elapsed(TimeUnit.MILLISECONDS), this.getPoolSize(), this.getCorePoolSize(), this.getActiveCount(),
            this.getCompletedTaskCount(), this.getTaskCount(), this.getQueue().size(), this.getLargestPoolSize(),
            this.getMaximumPoolSize(), this.getKeepAliveTime(TimeUnit.MILLISECONDS), this.isShutdown(),
            this.isTerminated()));
        if (Objects.nonNull(t)) {
            LOGGER.error(() -> "worker handle exception ", t);
        }
        stopwatch.stop();
    }

    /**
     * Perform a shutdown on the underlying ExecutorService.
     *
     * @see java.util.concurrent.ExecutorService#shutdown()
     * @see java.util.concurrent.ExecutorService#shutdownNow()
     */
    @Override
    public void shutdown() {
        LOGGER.info(() -> String.format("Shutting down ExecutorService :%s ", poolName));
        if (this.waitForTasksToCompleteOnShutdown) {
            super.shutdown();
        } else {
            super.shutdownNow();
        }
    }

}