package com.deepdhamala.filmpatro.auth.token.validator;

import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.refreshToken.RefreshTokenRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RefreshTokenValidator implements DbTokenValidator{

    private final RefreshTokenRepository repository;

    @Override
    public boolean isValid(Token token){
        return repository.findByRefreshToken(token.getToken())
                .map(t -> !t.isRevoked())
                .orElse(false);
    }

    @Override
    public boolean supports(TokenPurpose purpose) {
        return purpose == TokenPurpose.REFRESH;
    }
}
