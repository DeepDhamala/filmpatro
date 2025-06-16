package com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ForgetPasswordResetCodeValidator {

    private final ForgetPasswordResetCodeRepository forgetPasswordResetCodeRepository;

    public ForgetPasswordResetCode validateAndGet(String code) {

        ForgetPasswordResetCode forgetPasswordResetCode = forgetPasswordResetCodeRepository
                .findByForgetPasswordResetCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid or expired token"));

        if (forgetPasswordResetCode.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has already been used");
        }

        if (forgetPasswordResetCode.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expired");
        }

        return forgetPasswordResetCode;
    }
}
