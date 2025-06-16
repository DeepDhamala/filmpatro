package com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AuthorizationCodeValidator {

    private final AuthorizationCodeRepository authorizationCodeRepository;

    public AuthorizationCode validateAndGet(String authCode) {
        AuthorizationCode code = authorizationCodeRepository.findByAuthorizationCode(authCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Authorization code not found"));

        if (code.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization code already used");
        }
        if (code.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization code has expired");
        }

        return code;
    }
}