package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.auth.token.Token;
import io.jsonwebtoken.Claims;

import java.util.function.Function;

public interface JwtService extends ExtendedJwtService {

    Token generateToken(JwtTokenRequest jwtTokenRequest);
    <T> T extractClaim(Token token, Function<Claims, T> claimsResolver);
    boolean isTokenValid(Token token);

}
