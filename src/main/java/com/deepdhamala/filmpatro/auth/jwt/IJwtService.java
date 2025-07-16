package com.deepdhamala.filmpatro.auth.jwt;

import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;


/**
 * @deprecated Use {@link JwtService} instead.
 * This interface is deprecated and will be removed in future versions.
 */
@Deprecated
public interface IJwtService {
    @PostConstruct
    void init();

    String generateUserResourceAccessToken(UserDetails userDetails);

    String generateUserResourceAccessToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    );

    String generateUserResourceAccessToken(
            Map<String, Object> extraClaims,
            @NotNull UserDetails userDetails,
            long expiration
    );

    String generateRefreshToken(
            UserDetails userDetails
    );

    String extractUsernameFromJwt(String token);

    Date extractExpiration(String token);

    boolean isTokenValidInDb(String token);

    String fullValidateAndExtractUsername(String token);
}
