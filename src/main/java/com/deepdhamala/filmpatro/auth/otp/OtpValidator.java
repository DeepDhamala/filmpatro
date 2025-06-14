package com.deepdhamala.filmpatro.auth.otp;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OtpValidator {

    public void validateOtp(OtpToken otpToken, String providedOtp) {
        if (otpToken == null) {
            throw new InvalidOtpException("OTP not found");
        }
        if (!otpToken.getOtp().equals(providedOtp)) {
            throw new InvalidOtpException("Invalid OTP");
        }
        if (otpToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP has expired");
        }
        if (otpToken.isUsed()) {
            throw new InvalidOtpException("OTP has already been used");
        }

    }
}