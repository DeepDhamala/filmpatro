package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.auth.jwt.exception.JwtSecretKeyException;
import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.TokenManager;
import com.deepdhamala.filmpatro.auth.token.TokenPurpose;
import com.deepdhamala.filmpatro.auth.token.Tokens;
import com.deepdhamala.filmpatro.auth.token.accessToken.AccessTokenRepository;
import com.deepdhamala.filmpatro.auth.token.refreshToken.RefreshTokenRepository;
import com.deepdhamala.filmpatro.auth.token.validator.DbTokenValidator;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class PersistenceJwtService extends AbstractBasicJwtService implements JwtService2 {


    private final TokenManager tokenManager;
    private final List<DbTokenValidator> dbValidators;

    public PersistenceJwtService(
            JwtProperties jwtProperties,
            TokenManager tokenManager,
            List<DbTokenValidator> dbValidators
    ) {
        super(jwtProperties);
        this.tokenManager = tokenManager;
        this.dbValidators = dbValidators;
    }

    @PostConstruct
    public void init() {
        try {
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

    public Tokens issueAccessRefreshToken(JwtTokenRequest jwtTokenRequest) {
       Tokens tokens = generateTokens(jwtTokenRequest);
       return tokenManager.saveAccessAndRefreshTokens(tokens);
    }

    @Override
    protected boolean isValidTokenInternal(final Token token) {
        return dbValidators.stream()
                .filter(v -> v.supports(token.getTokenPurpose()))
                .findFirst()
                .map(v -> v.isValid(token))
                .orElse(false);
    }
}
