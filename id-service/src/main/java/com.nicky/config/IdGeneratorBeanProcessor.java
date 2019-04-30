/*
 * File Name:IdGeneratorBeanProcessor is created on 2019/3/22下午6:18 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.nicky.annotation.IdStrategy;
import com.nicky.constants.PropertySourcesConstants;
import com.nicky.util.BeanRegistrationUtil;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/22 下午6:18
 * @since JDK 1.8
 */
public class IdGeneratorBeanProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private ConfigurableEnvironment environment;

    private static final Multimap<Integer, String> NAMESPACE_NAMES = LinkedHashMultimap.create();



    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, IdGeneratorRegistrar.class.getName(),
            IdGeneratorRegistrar.class);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //todo nicky 根据配置文件修改
        initializePropertySources();

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    //PropertySourcesProcessor
    private void initializePropertySources() {
        if (environment.getPropertySources().contains(PropertySourcesConstants.APOLLO_PROPERTY_SOURCE_NAME)) {
            //already initialized
            return;
        }
        CompositePropertySource composite = new CompositePropertySource(PropertySourcesConstants.APOLLO_PROPERTY_SOURCE_NAME);

        //sort by order asc
        ImmutableSortedSet<Integer> orders = ImmutableSortedSet.copyOf(NAMESPACE_NAMES.keySet());
        Iterator<Integer> iterator = orders.iterator();

        while (iterator.hasNext()) {
            int order = iterator.next();
            for (String namespace : NAMESPACE_NAMES.get(order)) {
                //composite.addPropertySource();
            }
        }

        // clean up
        NAMESPACE_NAMES.clear();

        // add after the bootstrap property source or to the first
        if (environment.getPropertySources()
            .contains(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME)) {

            // ensure ApolloBootstrapPropertySources is still the first
           // ensureBootstrapPropertyPrecedence(environment);

            environment.getPropertySources()
                .addAfter(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME, composite);
        } else {
            environment.getPropertySources().addFirst(composite);
        }
    }

    public Set<Class<?>> scanClients(){

        Set<Class<?>> classes = new HashSet<>();

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        Resource[] resources = new Resource[0];
        try {
            resources = resourcePatternResolver.getResources("classpath*:com/xiaoyu/jyxb/admin/**/*.class");
        } catch (IOException e) {
        }

        for (Resource resource: resources){
            MetadataReader reader;
            try {
                reader = metadataReaderFactory.getMetadataReader(resource);
                if (reader.getAnnotationMetadata().hasAnnotation(IdStrategy.class.getName())){
                    Class<?> cls = ClassUtils.forName(reader.getClassMetadata().getClassName(), this.getClass().getClassLoader());
                    classes.add(cls);
                }
            } catch (Exception e) {
            }
        }
        return classes;
    }

//    public void setImportMetadata(AnnotationMetadata importMetadata) {
//        Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableIdGenerator.class.getName());
//
//        AnnotationAttributes attributes = AnnotationAttributes.fromMap(map);
//
//        this.model = Optional
//            .ofNullable(attributes.getString("model"))
//            .orElse("zookeeper");
//
//    }
}
