package com.deepdhamala.filmpatro.auth.jwt;

// document to say depends on jwtutils in api

import com.deepdhamala.filmpatro.auth.jwt.exception.BadJwtException;
import com.deepdhamala.filmpatro.auth.jwt.exception.JwtSecretKeyException;
import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.TokenPurpose;
import com.deepdhamala.filmpatro.auth.token.Tokens;
import com.deepdhamala.filmpatro.auth.token.accessToken.AccessToken;
import com.deepdhamala.filmpatro.auth.token.refreshToken.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.crypto.SecretKey;
import java.util.function.Function;


/**
 * Abstract base implementation of {@link JwtService} providing common JWT handling logic.
 *
 * <p>This class provides standard functionality to:
 * <ul>
 *     <li>Generate access and refresh tokens using a {@link JwtTokenRequest}</li>
 *     <li>Validate tokens using a combination of subclass-defined rules and cryptographic validation</li>
 * </ul>
 *
 * <p><b>Depends on:</b> {@link JwtUtils} for low-level JWT creation and validation logic.
 * This class delegates token generation and validation to {@code JwtUtils}, which must be properly
 * configured with a cryptographic {@link SecretKey} based on the configured {@link JwtProperties}.
 *
 * <p>Subclasses must implement {@link #isValidTokenInternal(Token)} to provide additional validation logic,
 * such as revocation checks or DB lookups.
 */
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractBasicJwtService extends AbstractJwtService {

    @Autowired
    private ApplicationContext applicationContext;
    protected SecretKey secretKey;

    @PostConstruct
    public void init() {
        try {
            JwtProperties jwtProperties = (JwtProperties) applicationContext.getBean("jwtProperties");
            byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
            if (keyBytes.length < 32) { // 256 bits for HS256
                throw new JwtSecretKeyException("JWT secret key must be at least 256 bits");
            }
            secretKey = Keys.hmacShaKeyFor(keyBytes);
            log.info("JWT secret key initialized successfully");
        } catch (IllegalArgumentException e) {
            log.error("Error initializing JWT secret key: {}", e.getMessage(), e);
            throw new JwtSecretKeyException("Failed to decode or initialize JWT secret key");
        }
    }
    @Override
    protected Token generateTokenInternal(JwtTokenRequest jwtTokenRequest) {
        String tokenString = JwtUtils.generateJwtToken(jwtTokenRequest);
        if (jwtTokenRequest.getTokenPurpose() == TokenPurpose.ACCESS) {
            return AccessToken.builder()
                    .token(tokenString)
                    .belongsTo(jwtTokenRequest.getUser())
                    .build();
        } else if (jwtTokenRequest.getTokenPurpose() == TokenPurpose.REFRESH) {
            return RefreshToken.builder()
                    .token(tokenString)
                    .belongsTo(jwtTokenRequest.getUser())
                    .build();
        }
        throw new IllegalArgumentException("Unknown token purpose");
    }

    @Override
    public Token generateAccessToken(JwtTokenRequest jwtTokenRequest) {
        jwtTokenRequest.setTokenPurpose(TokenPurpose.ACCESS);
        return generateToken(jwtTokenRequest);
    }

    @Override
    public Token generateRefreshToken(JwtTokenRequest jwtTokenRequest) {
        jwtTokenRequest.setTokenPurpose(TokenPurpose.REFRESH);
        return generateToken(jwtTokenRequest);
    }

    @Override
    public Tokens generateTokens(JwtTokenRequest jwtTokenRequest) {
        Token accessToken = generateAccessToken(jwtTokenRequest);
        Token refreshToken = generateRefreshToken(jwtTokenRequest);
        return Tokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public boolean isTokenValid(Token token) {
        return isValidTokenInternal(token) && JwtUtils.validateRawToken(token.getToken(), secretKey);
    }

    public <T> T extractClaim(Token token, Function<Claims, T> claimsResolver){
        try{
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token.getToken())
                    .getPayload();
            return claimsResolver.apply(claims);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadJwtException("Invalid or expired JWT token", e);
        }
    }

    protected abstract boolean isValidTokenInternal(Token token);
}
