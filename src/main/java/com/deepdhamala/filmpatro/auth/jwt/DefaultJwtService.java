package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.auth.jwt.exception.BadJwtException;
import com.deepdhamala.filmpatro.auth.jwt.exception.JwtRevokedException;
import com.deepdhamala.filmpatro.auth.jwt.exception.JwtSecretKeyException;
import com.deepdhamala.filmpatro.auth.token.accessToken.AccessTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultJwtService implements JwtService {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;
    private final AccessTokenRepository accessTokenRepository;

    @PostConstruct
    @Override
    public void init() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
            if (keyBytes.length < 32) { // 256 bits for HS256
                throw new JwtSecretKeyException("JWT secret key must be at least 256 bits");
            }
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
            log.info("JWT secret key initialized successfully");
        } catch (IllegalArgumentException e) {
            log.error("Error initializing JWT secret key: {}", e.getMessage(), e);
            throw new JwtSecretKeyException("Failed to decode or initialize JWT secret key");
        }
    }

    @Override
    public String generateUserResourceAccessToken(UserDetails userDetails) {
        return generateUserResourceAccessToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateUserResourceAccessToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return generateUserResourceAccessToken(extraClaims, userDetails, jwtProperties.getRefreshExpirationMillis());
    }

    @Override
    public String generateUserResourceAccessToken(
            Map<String, Object> extraClaims,
            @NotNull UserDetails userDetails,
            long expiration
    ) {
        return JwtUtils.generateAccessToken(extraClaims, userDetails.getUsername(), expiration, secretKey);
    }

    @Override
    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return JwtUtils.generateRefreshToken(userDetails.getUsername(), jwtProperties.getExpirationMillis(), secretKey);
    }

    @Override
    public String extractUsernameFromJwt(String token){
        return JwtUtils.extractClaim(token, secretKey, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token) {
        return JwtUtils.extractClaim(token, secretKey, Claims::getExpiration);
    }

    @Override
    public boolean isTokenValidInDb(String token) {
        return accessTokenRepository.findByAccessToken(token)
                .map(t -> !t.isRevoked())
                .orElse(false);
    }

    @Override
    public String fullValidateAndExtractUsername(String token) {
        String userEmail = extractUsernameFromJwt(token);
        if (userEmail == null) {
            throw new BadJwtException("Invalid token: username missing");
        }
        if (!isTokenValidInDb(token)) {
            throw new JwtRevokedException("Token revoked.");
        }
        return userEmail;
    }

}