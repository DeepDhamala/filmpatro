package com.deepdhamala.filmpatro.auth.oneTimeCode.otp;

import com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode.EmailVerificationCode;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class OtpValidator {

    public void validateOtp(EmailVerificationCode emailVerificationCode, String providedOtp) {
        if (emailVerificationCode == null) {
            throw new InvalidOtpException("OTP not found");
        }
        if (!emailVerificationCode.getEmailVerificationToken().equals(providedOtp)) {
            throw new InvalidOtpException("Invalid OTP");
        }
        if (emailVerificationCode.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidOtpException("OTP has expired");
        }
        if (emailVerificationCode.isUsed()) {
            throw new InvalidOtpException("OTP has already been used");
        }

    }
}