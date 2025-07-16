package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.auth.token.Token;
import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor
public abstract class AbstractJwtService implements JwtService {

    @Override
    public Token generateToken(JwtTokenRequest jwtTokenRequest) {
        return generateTokenInternal(jwtTokenRequest);
    }

    @Override
    public abstract <T> T extractClaim(Token token, Function<Claims, T> claimsResolver);

    protected abstract Token generateTokenInternal(JwtTokenRequest jwtTokenRequest);

}
