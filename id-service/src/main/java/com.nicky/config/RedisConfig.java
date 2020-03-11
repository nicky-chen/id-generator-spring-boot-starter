package com.nicky.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/1 at 17:42
 */
@Configuration
public class RedisConfig {

    //https://my.oschina.net/dengfuwei/blog/1604975

    private String address = "redis://localhost:6379";
    private int connectionMinimumIdleSize = 10;
    private int idleConnectionTimeout = 10000;
    private int pingTimeout = 1000;
    private int connectTimeout = 10000;
    private int timeout = 3000;
    private int retryAttempts = 3;
    private int retryInterval = 1500;
    private int reconnectionTimeout = 3000;
    private int failedAttempts = 3;
    private String password = "0808";
    private int subscriptionsPerConnection = 5;
    private String clientName = "test";
    private int subscriptionConnectionMinimumIdleSize = 1;
    private int subscriptionConnectionPoolSize = 50;
    private int connectionPoolSize = 64;
    private int database = 0;
    private boolean dnsMonitoring = false;
    private int dnsMonitoringInterval = 5000;

    private int thread; //当前处理核数量 * 2

    private String codec = "org.redisson.codec.JsonJacksonCodec";

    @ConditionalOnMissingBean
    public RedissonClient init() {

        Config config = new Config();
        config.useSingleServer().setAddress(address).setConnectionMinimumIdleSize(connectionMinimumIdleSize)
            .setConnectionPoolSize(connectionPoolSize).setDatabase(database)
            .setDnsMonitoringInterval(dnsMonitoringInterval)
            .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
            .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
            .setSubscriptionsPerConnection(subscriptionsPerConnection).setClientName(clientName)
            .setFailedAttempts(failedAttempts).setRetryAttempts(retryAttempts).setRetryInterval(retryInterval)
            .setReconnectionTimeout(reconnectionTimeout).setTimeout(timeout).setConnectTimeout(connectTimeout)
            .setIdleConnectionTimeout(idleConnectionTimeout).setPingTimeout(pingTimeout).setPassword(password);
        config.setThreads(thread);
        config.setCodec(new LongCodec());
        config.setEventLoopGroup(new NioEventLoopGroup());
        config.setUseLinuxNativeEpoll(false);
        return Redisson.create(config);
    }

}
