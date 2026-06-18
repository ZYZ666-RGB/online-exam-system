package com.example.exam.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "exam.auth")
public class AuthProperties {

    private String jwtSecret;
    private Long tokenExpireMinutes;
    private String tokenPrefix;
    private String redisTokenPrefix;
    private String loginFailPrefix;
    private String registerLimitPrefix;
}

