/*
 * File Name:ZookeeperGeneratorConfiguration is created on 2019/4/7上午11:10 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/4/7 上午11:10
 * @since JDK 1.8
 */
public class ZookeeperGeneratorConfig {

    private ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

    private static final String COUNTER_ZNODE = "/yqlock_pathDistAtomicLong";

    public ZkIdGeneratorConfig zookeeperIdGeneratorConfig() {
        return new ZkIdGeneratorConfig();
    }

    //@Bean
    public CuratorFramework curatorFramework(ZkIdGeneratorConfig config) {

        CuratorFrameworkFactory.Builder builder =
            CuratorFrameworkFactory.builder().connectionTimeoutMs(5000).sessionTimeoutMs(15000).retryPolicy(retryPolicy)
                .connectString(config.getConnectUrls());

        if (StringUtils.isNotBlank(config.getNamespace())) {
            builder.namespace(config.getNamespace());
        }

        CuratorFramework client = builder.build();
        client.start();
        return client;
    }

    public DistributedAtomicLong distributedAtomicLong(CuratorFramework client) {
        return new DistributedAtomicLong(client, COUNTER_ZNODE, retryPolicy);
    }
}
