package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.auth.token.TokenType;
import com.deepdhamala.filmpatro.auth.token.accessToken.AccessToken;
import com.deepdhamala.filmpatro.auth.token.accessToken.AccessTokenRepository;
import com.deepdhamala.filmpatro.auth.token.refreshToken.RefreshToken;
import com.deepdhamala.filmpatro.auth.token.refreshToken.RefreshTokenRepository;
import com.deepdhamala.filmpatro.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private SecretKey secretKey;
    private final AccessTokenRepository accessTokenRepository;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        if (keyBytes.length < 32) { // 256 bits for HS256
            throw new IllegalArgumentException("JWT secret key must be at least 256 bits");
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsernameFromJwt(String token){
        return extractClaimFromJwt(token, Claims::getSubject);
    }

    public <T> T extractClaimFromJwt(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsFromJwt(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaimsFromJwt(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token: {}", e.getMessage());
            throw new JwtAuthenticationException("JWT token has expired", e);
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid JWT token", e);
        }
    }


    public String generateUserResourceAccessToken(UserDetails userDetails) {
        return generateUserResourceAccessToken(new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return generateUserResourceAccessToken(new HashMap<>(), userDetails, jwtProperties.getExpirationMillis());
    }

    public String generateUserResourceAccessToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return generateUserResourceAccessToken(extraClaims, userDetails, jwtProperties.getRefreshExpirationMillis());
    }

    private String generateUserResourceAccessToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Date extractExpiration(String token) {
        return extractClaimFromJwt(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsernameFromJwt(token);
        return (username.equals(userDetails.getUsername())) && !isTokenNonExpired(token);
    }

    private boolean isTokenNonExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String validateAndExtractUsername(String token) {

        String userEmail = extractUsernameFromJwt(token);
        if (userEmail == null) {
            throw new JwtAuthenticationException("Invalid token: username missing");
        }

        var valid = accessTokenRepository.findByAccessToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);

        if (!valid) {
            throw new JwtAuthenticationException("Token revoked or expired.");
        }

        return userEmail;
    }

    public void saveUserToken(User user, String accessToken, String refreshToken) {
        var accessTokenEntity = AccessToken.builder()
                .user(user)
                .accessToken(accessToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        var refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .refreshToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
        accessTokenRepository.save(accessTokenEntity);
    }

}
