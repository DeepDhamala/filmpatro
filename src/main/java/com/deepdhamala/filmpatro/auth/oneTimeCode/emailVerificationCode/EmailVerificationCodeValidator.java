package com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationCodeValidator {
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;

    public EmailVerificationCode validateAndGet(String token) {
        EmailVerificationCode verificationToken = emailVerificationCodeRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid verification token"));

        if (verificationToken.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token already used");
        }

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expired");
        }

        return verificationToken;
    }
}
