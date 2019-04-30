/*
 * File Name:LockFilter is created on 2019/4/21上午11:31 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.filter;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import javax.sql.DataSource;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/4/21 上午11:31
 * @since JDK 1.8
 */
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class LockFilter implements Lock, RuleFilter{

    private DataSource dataSource;

    private String lockSql;

    //private static final String cmd = "SELECT * FROM lock WHERE uid = 1 FOR UPDATE";

    private static ThreadLocal<Connection> threadLocal = ThreadLocal.withInitial(() -> null);

    private static ListeningExecutorService threadPool = MoreExecutors.listeningDecorator(
        new ThreadPoolExecutor(4, 8, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20),
            new ThreadFactoryBuilder().setNameFormat("sql-%d").build()));

    @Override
    public void lock() {
        try {
            //try get lock
            log.info("{} begin try lock", Thread.currentThread().getName());
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            threadLocal.set(conn);
            PreparedStatement stmt = conn.prepareStatement(lockSql);
            stmt.executeQuery();
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void lockInterruptibly() {
        //todo nicky
    }

    @Override
    public boolean tryLock() {
        try {
            //try get lock
            log.info("{} begin try lock", Thread.currentThread().getName());
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            threadLocal.set(conn);
            PreparedStatement stmt = conn.prepareStatement(lockSql);
            //多个线程执行 rs = stmt.executeQuery();时候，只有一个线程会获取到行锁，其它线程被阻塞挂
            stmt.executeQuery();
            return true;
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        try {
            //try get lock
            log.info("{} begin try lock", Thread.currentThread().getName());
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            threadLocal.set(conn);
            PreparedStatement stmt = conn.prepareStatement(lockSql);
            //多个线程执行 rs = stmt.executeQuery();时候，只有一个线程会获取到行锁，其它线程被阻塞挂
            threadPool.submit(() -> stmt.executeQuery()).get(time, unit);
            return true;
        } catch (SQLException | InterruptedException | ExecutionException | TimeoutException e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public void unlock() {
        try (Connection connection = threadLocal.get()) {
            if (connection == null) {
                throw new ConnectException("connection is invalid");
            }
            log.info(" {} release lock", Thread.currentThread().getName());
            connection.commit();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }

    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List filterData(List ruleList) {
        return null;
    }
}
