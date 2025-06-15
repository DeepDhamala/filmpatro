package com.deepdhamala.filmpatro.auth.token.emailVerification;

import com.deepdhamala.filmpatro.auth.AuthenticationService;
import com.deepdhamala.filmpatro.auth.userAuth.UserAuthenticationResponseDto;
import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {
    private final OtpValidator otpValidator;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;
    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;
    

    public String generateOtp() {
        int otp = 100_000 + random.nextInt(900_000); // Generates a 6-digit number
        return String.valueOf(otp);
    }

    public EmailVerificationToken saveOtp(User user, String otp) {
        EmailVerificationToken emailVerificationToken = EmailVerificationToken.builder()
                .emailVerificationToken(otp)
                .user(user)
                .expiryDate(java.time.LocalDateTime.now().plusMinutes(5)) // OTP valid for 5 minutes
                .build();
        return emailVerificationTokenRepository.save(emailVerificationToken);
    }

    public EmailVerificationToken prepareEmailVerificationToken(User recentlyRegisteredUser) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken emailVerificationToken = EmailVerificationToken.builder()
                .emailVerificationToken(token)
                .user(recentlyRegisteredUser)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
        return emailVerificationTokenRepository.save(emailVerificationToken);
    }




    public void verifyEmail(String emailVerificationToken) {
        var verificationToken = emailVerificationTokenRepository.findByEmailVerificationToken(emailVerificationToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid verification token"));

        if (verificationToken.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Token already used");
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Token has expired");
        }

        var user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        verificationToken.setUsed(true);
        emailVerificationTokenRepository.save(verificationToken);
    }
}
