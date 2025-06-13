package com.deepdhamala.filmpatro.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "application.security.jwt")
@Component
@Data
public class JwtProperties {
    private String secret;
    private long expirationMillis;
    private long refreshExpirationMillis;
}