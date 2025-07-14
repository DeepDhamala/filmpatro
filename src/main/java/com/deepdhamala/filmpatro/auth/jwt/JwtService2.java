package com.deepdhamala.filmpatro.auth.jwt;

import io.jsonwebtoken.Jwt;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService2 {
    String generateToken(JwtTokenRequest jwtTokenRequest);
    <T> T extractClaim(String token, String claimName, Class<T> claimType);
}
