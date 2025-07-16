package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.auth.token.TokenPurpose;
import com.deepdhamala.filmpatro.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Map;

@RequiredArgsConstructor
@Data
@Builder
public class JwtTokenRequest {
    private final Map<String, Object> extraClaims;
    private final User user;
    private final long expiration;
    private final SecretKey secretKey;
    private TokenPurpose tokenPurpose = TokenPurpose.ACCESS;
    private final String subject;

    public static JwtTokenRequest withDefaults() {
        return JwtTokenRequest.builder()
                .build();
    }
}