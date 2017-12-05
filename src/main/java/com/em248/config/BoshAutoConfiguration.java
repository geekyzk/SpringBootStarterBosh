package com.em248.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2017/12/5.
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({BoshProperties.class})
public class BoshAutoConfiguration {

    private final BoshProperties properties;


    public BoshAutoConfiguration(BoshProperties properties) {
        this.properties = properties;
    }




}
