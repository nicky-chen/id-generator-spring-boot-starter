/*
 * File Name:ObjectPool is created on 2019/3/26上午10:32 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author nicky_chin
 * @description: 调用service 连接池
 * @date: 2019/3/26 上午10:32
 * @since JDK 1.8
 */
public class RemoteConnectionPool {

    // TODO
    private final static String URL = "jdbc:mysql://localhost:3306/activiti";
    private final static String USERNAME = "root";
    private final static String PASSWD = "root";
    private final static String DRIVER = "com.mysql.jdbc.Driver";

    private volatile static RemoteConnectionPool pool;

    public static RemoteConnectionPool getInstance() {
        if (pool == null) {
            synchronized (RemoteConnectionPool.class) {
                if (pool == null) {
                    pool = new RemoteConnectionPool();
                }
            }
        }
        return pool;
    }

    private static GenericObjectPool<Connection> connPool;

    private RemoteConnectionPool() {
        connPool = new GenericObjectPool<Connection>(new JDBCPooledFactory(), getDefaultConfig());
    }

    private GenericObjectPoolConfig getDefaultConfig() {
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        // TODO -- 默认8,8,0
        conf.setMaxTotal(50);
        conf.setMaxIdle(50);
        conf.setMinIdle(0);
        conf.setMaxWaitMillis(60000);
        return conf;
    }

    public RemoteConnectionPool(GenericObjectPoolConfig config) {
        if (config == null)
            config = getDefaultConfig();
        connPool = new GenericObjectPool<Connection>(new JDBCPooledFactory(), config);
    }

    public GenericObjectPool<Connection> getConnectionPool() {
        return connPool;
    }

    public Connection getConnection() throws Exception {
        return getConnectionPool().borrowObject();
    }

    public static void closePsAndRs(PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                rs = null;
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                ps = null;
            }
        }
    }

    public static void returnConnection(Connection conn) {
        connPool.returnObject(conn);
    }

    public static void returnConnectionAndClose(Connection conn, PreparedStatement ps, ResultSet rs) {
        closePsAndRs(ps, rs);
        returnConnection(conn);
    }

    static class JDBCPooledFactory extends BasePooledObjectFactory<Connection> {
        static {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Connection create() throws Exception {
            return DriverManager.getConnection(URL, USERNAME, PASSWD);
        }

        @Override
        public PooledObject<Connection> wrap(Connection arg0) {
            return new DefaultPooledObject<Connection>(arg0);
        }
    }

    public static void main(String[] args) {
        try {
            for (int i = 0; i < 200; i++) {
                //               // 1 单线程不归还连接
                //               Connection conn = RemoteConnectionPool.getInstance().getConnection();
                //               System.out.println(conn.hashCode());
                //               // 2 单线程归还连接
                //               RemoteConnectionPool.returnConnection(conn);
                // 3 多线程不归还
                new Thread(new PoolTestThread()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class PoolTestThread implements Runnable {

        @Override
        public void run() {
            try {
                Connection conn = RemoteConnectionPool.getInstance().getConnection();
                System.out.println(Thread.currentThread().getName() + " : " + conn.hashCode());
                // 4 多线程归还连接
                Thread.sleep(1000);
                RemoteConnectionPool.returnConnection(conn);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
