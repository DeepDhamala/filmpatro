package com.deepdhamala.filmpatro.auth;

import com.deepdhamala.filmpatro.auth.token.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;
    private SecretKey secretKey;
    private final TokenRepository tokenRepository;

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
        } catch (JwtException e){
            throw new IllegalArgumentException("Invalid JWT token", e);
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

    private Date extractExpiration(String token) {
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
        // Validate JWT syntax, signature, and expiry
        String userEmail = extractUsernameFromJwt(token);
        if (userEmail == null) {
            throw new IllegalArgumentException("Invalid token: username missing");
        }
        // Validate DB
        var valid = tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
        if (!valid) {
            throw new IllegalArgumentException("Token revoked or expired in database");
        }
        return userEmail;
    }

}
