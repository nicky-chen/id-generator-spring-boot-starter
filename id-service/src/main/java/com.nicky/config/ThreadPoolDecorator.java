/*
 * File Name:ThreadPoolDecorator is created on 2019/5/30上午10:17 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.nicky.model.LoggerEnhancer;
import com.vip.vjtools.vjkit.concurrent.ThreadDumpper;

import jodd.util.StringPool;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author nicky_chin
 * @description: 线程池装饰器监控类
 * @date: 2019/5/30 上午10:17
 * @since JDK 1.8
 */
@Getter
public class ThreadPoolDecorator extends ThreadPoolExecutor implements DisposableBean {

    private static final LoggerEnhancer LOGGER = LoggerEnhancer.getLogger(ThreadPoolDecorator.class);

    /**
     * 线程池
     */
    private ThreadPoolExecutor executor;

    /**
     * 是否任务结束shutdown
     */
    private boolean waitForTasksToCompleteOnShutdown;

    /**
     * 线程池终止时间
     */
    private long awaitTerminationSeconds;

    /**
     * 线程池名称
     */
    private String poolName;

    /**
     * 计时器
     */
    private ConcurrentMap<String, Stopwatch> timer;

    private ThreadPoolDecorator(ThreadPoolExecutor executor, long awaitTerminationSeconds) {
        this(executor.getCorePoolSize(), executor.getMaximumPoolSize(), executor.getKeepAliveTime(TimeUnit.SECONDS),
            TimeUnit.SECONDS, executor.getQueue(), executor.getThreadFactory(),
            ObjectUtils.defaultIfNull(executor.getRejectedExecutionHandler(), new AbortPolicy()));
        this.executor = executor;
        this.awaitTerminationSeconds = awaitTerminationSeconds;
        this.timer = Maps.newConcurrentMap();
    }

    private ThreadPoolDecorator(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 装饰方法
     *
     * @param executor
     * @param awaitTerminationSeconds
     *
     * @return
     */
    public static ThreadPoolDecorator warp(ThreadPoolExecutor executor, long awaitTerminationSeconds) {
        return new ThreadPoolDecorator(executor, awaitTerminationSeconds);
    }

    /**
     * 线程池名称
     *
     * @param poolName
     *
     * @return
     */
    public ThreadPoolDecorator poolName(String poolName) {
        this.poolName = StringUtils.defaultString(poolName, StringPool.EMPTY);
        return this;
    }

    /**
     * 任务结束停机方式
     *
     * @param waitForTasksToCompleteOnShutdown
     *
     * @return
     */
    public ThreadPoolDecorator waitForTasksToComplete(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
        return this;
    }

    /**
     * 转换带成callback的executor
     *
     * @return
     */
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
            LOGGER.error("worker handle exception ", t);
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
        LOGGER.info("Shutting down ExecutorService :{}", poolName);
        if (this.waitForTasksToCompleteOnShutdown) {
            super.shutdown();
            return;
        }
        super.shutdownNow();
    }

    @Override
    public void destroy() {
        this.shutdown();
        try {
            super.awaitTermination(this.awaitTerminationSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * AbortPolicy策略增加dump日志
     */
    @RequiredArgsConstructor(staticName = "of")
    public static class AbortPolicyEnhancer extends AbortPolicy {

        /**
         * 线程名称
         */
        @NonNull
        private final String threadName;

        /**
         * 打印堆栈对多8层
         */
        private static final int MAX_STACK_LEVEL = 8;

        /**
         * dump日志间隔时间最小15分钟
         */
        private static final int MIN_INTERVAL = 1000 * 60 * 15;

        private ThreadDumpper threadDumpper = new ThreadDumpper(MAX_STACK_LEVEL, MIN_INTERVAL);

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            String msg = String.format("Export thread pools bounds had being exceeded exception!"
                    + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
                    + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)!", threadName, e.getPoolSize(),
                e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
            threadDumpper.tryThreadDump(null);
            throw new RejectedExecutionException(msg);
        }

    }

}