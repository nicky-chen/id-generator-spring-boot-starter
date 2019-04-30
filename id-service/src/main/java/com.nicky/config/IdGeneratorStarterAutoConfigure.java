package com.nicky.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nicky.service.StarterService;

@Configuration
@ConditionalOnClass(StarterService.class)
@EnableConfigurationProperties(StarterServiceProperties.class)
public class IdGeneratorStarterAutoConfigure {

    @Autowired
    private StarterServiceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "default.string", value = "enabled", havingValue = "true")
    StarterService starterService (){
        return new StarterService(properties.getConfig());
    }

    @Bean
    IdGeneratorBeanProcessor idGeneratorBeanProcessor() {
        return new IdGeneratorBeanProcessor();
    }

}
