package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.TokenManager;
import com.deepdhamala.filmpatro.auth.token.Tokens;
import com.deepdhamala.filmpatro.auth.token.accessToken.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersistenceJwtService extends AbstractDbJwtService {

    private TokenManager tokenManager;
    private AccessTokenRepository accessTokenRepository;

    @Override
    public Tokens saveTokens(Tokens tokens) {
        return tokenManager.saveAccessAndRefreshTokens(tokens);
    }

    @Override
    protected boolean isValidTokenInternal(Token token) {
        return accessTokenRepository.findByAccessToken(token.getToken())
                .map(t -> !t.isRevoked())
                .orElse(false);
    }
}
