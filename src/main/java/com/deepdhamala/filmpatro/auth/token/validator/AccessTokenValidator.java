package com.deepdhamala.filmpatro.auth.token.validator;

import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.TokenPurpose;
import com.deepdhamala.filmpatro.auth.token.accessToken.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenValidator implements DbTokenValidator {

    private final AccessTokenRepository repository;

    @Override
    public boolean isValid(Token token) {
        return repository.findByAccessToken(token.getToken())
                .map(t -> t.isRevoked())
                .orElse(false);
    }

    @Override
    public boolean supports(TokenPurpose purpose) {
        return purpose == TokenPurpose.ACCESS;
    }
}
