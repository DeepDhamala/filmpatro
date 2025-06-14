package com.deepdhamala.filmpatro.auth.otp;

import com.deepdhamala.filmpatro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

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

    public OtpToken saveOtp(User user, String otp) {
        OtpToken otpToken = OtpToken.builder()
                .otp(otp)
                .user(user)
                .expiresAt(java.time.LocalDateTime.now().plusMinutes(5)) // OTP valid for 5 minutes
                .used(false)
                .build();
        return otpRepository.save(otpToken);
    }


}
