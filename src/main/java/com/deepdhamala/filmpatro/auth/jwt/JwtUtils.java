package com.deepdhamala.filmpatro.auth.jwt;


import com.deepdhamala.filmpatro.auth.jwt.exception.BadJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class JwtUtils {

    private JwtUtils() {
        // Private constructor to prevent instantiation
    }

    public static String generateAccessToken(Map<String, Object> extraClaims, String subject, long expirationMillis, SecretKey secretKey) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public static String generateRefreshToken(String subject, long expirationMillis, SecretKey secretKey) {
        return Jwts
                .builder()
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public static Claims extractAllClaims(String token, SecretKey secretKey) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new BadJwtException("Invalid or expired JWT token", e);
        }
    }

    public static <T> T extractClaim(String token, SecretKey secretKey, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    public static boolean isTokenExpired(String token, SecretKey secretKey) {
        return extractClaim(token, secretKey, Claims::getExpiration).before(new Date());
    }

    public static boolean validateRawToken(String token, SecretKey secretKey) {
        try {
            extractAllClaims(token, secretKey);
            return true;
        } catch (BadJwtException e) {
            return false;
        }
    }
}