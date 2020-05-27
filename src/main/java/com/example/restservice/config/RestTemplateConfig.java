package com.example.restservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tolim on 3/07/2018.
 */
@Configuration
@Slf4j
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder,
                                     @Value("${resttemplate.connect.timeout}") long connectionTimeOut,
                                     @Value("${resttemplate.read.timeout}") long readTimeout) {

        log.info("Creating rest template");
        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(connectionTimeOut))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        //interceptors.add(new RestTemplateInterceptor());
        restTemplate.setInterceptors(interceptors);

        log.info("Rest template created");
        return restTemplate;
    }
}