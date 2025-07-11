package com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeService;
import com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode.AuthorizationCodeEntity;
import com.deepdhamala.filmpatro.email.EmailService;
import com.deepdhamala.filmpatro.email.message.PasswordResetEmailMessage;
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
public class ForgetPasswordRecoveryService implements OneTimeCodeService<ForgetPasswordRecoveryCode> {

    private final ForgetPasswordResetCodeRepository forgetPasswordResetCodeRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ForgetPasswordResetCodeValidator forgetPasswordResetCodeValidator;


    @Override
    public ForgetPasswordRecoveryCode generate(User user) {
        String code = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(15*60);

        return ForgetPasswordRecoveryCode.builder()
                .user(user)
                .code(code)
                .expiresAt(expiresAt)
                .build();
    }

    @Override
    @Transactional
    public ForgetPasswordRecoveryCode save(ForgetPasswordRecoveryCode oneTimeCode) {
        ForgetPasswordRecoveryCodeEntity forgetPasswordResetCode = ForgetPasswordRecoveryCodeEntity.builder()
                .forgetPasswordResetCode(oneTimeCode.getCode())
                .codeType(oneTimeCode.getType())
                .user(oneTimeCode.getUser())
                .expiresAt(oneTimeCode.getExpiresAt())
                .build();
        forgetPasswordResetCodeRepository.save(forgetPasswordResetCode);
        return oneTimeCode;
    }

    @Override
    @Transactional
    public ForgetPasswordRecoveryCode issue(User user) {
        return save(generate(user));
    }


    @Transactional
    public void sendForgetPasswordRecoveryEmail(ForgotPasswordRequestDto forgotPasswordRequestDto) {

        var user = userService.getByEmailOrThrow(forgotPasswordRequestDto.getEmail());

        ForgetPasswordRecoveryCode forgetPasswordResetCode = issue(user);

        emailService.sendEmail(new PasswordResetEmailMessage(forgetPasswordResetCode));

    }

    @Transactional
    public void resetForgottenPassword(ForgetPasswordResetRequestDto forgetPasswordResetRequestDto) {

        ForgetPasswordRecoveryCodeEntity forgetPasswordResetCode = forgetPasswordResetCodeValidator.validateAndGet(forgetPasswordResetRequestDto.getForgetPasswordResetCode());

        var user = forgetPasswordResetCode.getUser();
        user.setPassword(passwordEncoder.encode(forgetPasswordResetRequestDto.getPassword()));

        forgetPasswordResetCode.setUsed(true);
    }
}
