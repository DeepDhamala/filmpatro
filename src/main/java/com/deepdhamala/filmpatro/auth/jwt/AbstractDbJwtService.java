package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.auth.token.Tokens;

public abstract class AbstractDbJwtService extends AbstractBasicJwtService implements DbJwtService {


    public Tokens generateTokens(JwtTokenRequest jwtTokenRequest) {
        Tokens tokens = super.generateTokens(jwtTokenRequest);
        saveTokens(tokens);
        return tokens;
    }

     public abstract Tokens saveTokens(Tokens tokens);
}
