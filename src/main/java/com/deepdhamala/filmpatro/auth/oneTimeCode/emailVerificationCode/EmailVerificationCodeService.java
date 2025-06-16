package com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeType;
import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationCodeService {
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;
    private final EmailVerificationCodeValidator emailVerificationCodeValidator;


    public EmailVerificationCode issueEmailVerificationCode(User recentlyRegisteredUser) {

        String code = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(15*60); // 15 minutes

        EmailVerificationCode emailVerificationCode = EmailVerificationCode.builder()
                .emailVerificationToken(code)
                .user(recentlyRegisteredUser)
                .codeType(OneTimeCodeType.UUID)
                .expiresAt(expiresAt)
                .build();

        return emailVerificationCodeRepository.save(emailVerificationCode);
    }

    @Transactional
    public void verifyRegisteredEmail(String emailVerificationToken) {

        EmailVerificationCode verificationCode = emailVerificationCodeValidator.validateAndGet(emailVerificationToken);

        verificationCode.setUsed(true);

        var user = verificationCode.getUser();
        user.setEnabled(true);
    }
}
