package com.deepdhamala.filmpatro.auth.jwt;

// document to say depends on jwtutils in api

import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.Tokens;
import com.deepdhamala.filmpatro.auth.token.accessToken.AccessToken;
import com.deepdhamala.filmpatro.auth.token.refreshToken.RefreshToken;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;


/**
 * Abstract base implementation of {@link JwtService2} providing common JWT handling logic.
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
public abstract class AbstractBasicJwtService extends AbstractJwtService implements JwtService2 {

    protected final JwtProperties jwtProperties;
    protected SecretKey secretKey;

    @Override
    protected String generateTokenInternal(JwtTokenRequest jwtTokenRequest) {
        return JwtUtils.generateJwtToken(jwtTokenRequest);
    }

    public Tokens generateTokens(JwtTokenRequest jwtTokenRequest) {
        String accessToken = generateToken(jwtTokenRequest);
        String refreshToken = generateRefreshToken(jwtTokenRequest);
        return Tokens.builder()
                .accessToken(new AccessToken(accessToken, jwtTokenRequest.getUser()))
                .refreshToken(new RefreshToken(refreshToken, jwtTokenRequest.getUser()))
                .build();
    }

    public boolean isValidToken(Token token) {
        return isValidTokenInternal(token) && JwtUtils.validateRawToken(token.getToken(), secretKey);
    }

    protected abstract boolean isValidTokenInternal(Token token);
}
