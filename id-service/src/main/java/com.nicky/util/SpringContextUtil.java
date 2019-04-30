package com.nicky.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.nicky.model.adapter.LoggerEnhancer;

/**
 * 描述 : spring的上下文工具
 *
 * @author zhaoxl
 * @date 2018/8/14 上午9:00
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static final LoggerEnhancer LOGGER = LoggerEnhancer.getLogger(SpringContextUtil.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LOGGER.info("spring startup and inject it's context.");
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过bean名称获取bean
     *
     * @param name bean的名称
     *
     * @return
     *
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 通过class来获取bean
     *
     * @param cls class
     * @param <T>
     *
     * @return
     *
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> cls) throws BeansException {
        return applicationContext.getBean(cls);
    }

}
