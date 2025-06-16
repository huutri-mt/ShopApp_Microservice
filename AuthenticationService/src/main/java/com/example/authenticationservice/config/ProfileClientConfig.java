package com.example.authenticationservice.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileClientConfig {
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(
            @Value("${auth.username}") String username,
            @Value("${auth.password}") String password) {
        return new BasicAuthRequestInterceptor(username, password);
    }
}
