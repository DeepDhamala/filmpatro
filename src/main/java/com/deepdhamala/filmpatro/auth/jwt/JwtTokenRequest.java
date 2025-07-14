package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Map;

@AllArgsConstructor
@Data
public class JwtTokenRequest {
    private final Map<String, Object> extraClaims;
    private final User user;
    private final long expiration;
    private final SecretKey secretKey;
    private final String type = "access";
    private final String subject;
}