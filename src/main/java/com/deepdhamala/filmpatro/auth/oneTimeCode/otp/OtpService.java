package com.deepdhamala.filmpatro.auth.oneTimeCode.otp;

import com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode.EmailVerificationCode;
import com.deepdhamala.filmpatro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    private final OtpRepository otpRepository;

    public String generateOtp() {
        int otp = 100_000 + random.nextInt(900_000); // Generates a 6-digit number
        return String.valueOf(otp);
    }

    public Otp saveOtp(User user, String otp) {
        Otp otpToken = Otp.builder()
                .otp(otp)
                .user(user)
                .expiresAt(Instant.now().plusSeconds(2*60)) // OTP valid for 5 minutes
                .build();
        return otpRepository.save(otpToken);
    }
}
