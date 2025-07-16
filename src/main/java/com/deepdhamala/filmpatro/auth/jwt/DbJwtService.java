package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.auth.token.Tokens;

public interface DbJwtService {
    Tokens saveTokens(Tokens tokens);
}
