package com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode;

import com.deepdhamala.filmpatro.email.EmailService;
import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgetPasswordResetCodeService {

    private final ForgetPasswordResetCodeRepository forgetPasswordResetCodeRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ForgetPasswordResetCodeValidator forgetPasswordResetCodeValidator;

    public void recoverForgetPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {

        User user = userService.getByEmailOrThrow(forgotPasswordRequestDto.getEmail());

        String resetCode = issueForgetPasswordResetCode(user);
        String resetLink = buildPasswordResetLink(resetCode);

        sendPasswordResetEmail(user.getEmail(), resetLink);

    }

    @Transactional
    public void resetForgetPassword(ForgetPasswordResetRequestDto forgetPasswordResetRequestDto) {

        ForgetPasswordResetCode forgetPasswordResetCode = forgetPasswordResetCodeValidator.validateAndGet(forgetPasswordResetRequestDto.getForgetPasswordResetCode());

        var user = forgetPasswordResetCode.getUser();
        user.setPassword(passwordEncoder.encode(forgetPasswordResetRequestDto.getPassword()));

        forgetPasswordResetCode.setUsed(true);
    }

    private String issueForgetPasswordResetCode(User user) {
        String code = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(15*60);

        ForgetPasswordResetCode resetCode = ForgetPasswordResetCode.builder()
                .user(user)
                .forgetPasswordResetCode(code)
                .expiresAt(expiresAt)
                .build();

        forgetPasswordResetCodeRepository.save(resetCode);
        return code;
    }

    private String buildPasswordResetLink(String resetCode) {
        return "https://localhost:8080/?forgetPasswordRecoverytoken=" + resetCode;
    }

    private void sendPasswordResetEmail(String email, String resetLink) {
        emailService.sendPasswordResetEmailHtml(email, resetLink);
    }
}
