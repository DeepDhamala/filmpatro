package com.deepdhamala.filmpatro.auth.token.emailVerification;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OtpValidator {

    public void validateOtp(EmailVerificationToken emailVerificationToken, String providedOtp) {
        if (emailVerificationToken == null) {
            throw new InvalidOtpException("OTP not found");
        }
        if (!emailVerificationToken.getEmailVerificationToken().equals(providedOtp)) {
            throw new InvalidOtpException("Invalid OTP");
        }
        if (emailVerificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP has expired");
        }
        if (emailVerificationToken.isUsed()) {
            throw new InvalidOtpException("OTP has already been used");
        }

    }
}