package com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodePurpose;
import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeService;
import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeType;
import com.deepdhamala.filmpatro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationCodeService implements OneTimeCodeService<EmailVerificationCode>{

    private final EmailVerificationCodeRepository emailVerificationCodeRepository;

    private final EmailVerificationCodeValidator emailVerificationCodeValidator;


    @Override
    public EmailVerificationCode generate(User user) {

        String code = UUID.randomUUID().toString();

        Instant expiresAt = Instant.now().plusSeconds(15*60); // 15 minutes

        return EmailVerificationCode.builder()
                .user(user)
                .code(code)
                .type(OneTimeCodeType.UUID)
                .purpose(OneTimeCodePurpose.EMAIL_VERIFICATION)
                .expiresAt(expiresAt)
                .build();
    }


    @Override
    @Transactional
    public EmailVerificationCode save(EmailVerificationCode oneTimeCode) {

        EmailVerificationCodeEntity emailVerificationCodeEntity = EmailVerificationCodeEntity.builder()
                .emailVerificationToken(oneTimeCode.getCode())
                .codeType(oneTimeCode.getType())
                .user(oneTimeCode.getUser())
                .expiresAt(oneTimeCode.getExpiresAt())
                .build();

        emailVerificationCodeRepository.save(emailVerificationCodeEntity);

        return oneTimeCode;
    }

    @Override
    @Transactional
    public EmailVerificationCode issue(User user) {
        EmailVerificationCode oneTimeCode = generate(user);
        return save(oneTimeCode);
    }

    @Transactional
    public void verifyRegisteredEmail(String emailVerificationToken) {

        EmailVerificationCodeEntity verificationCode = emailVerificationCodeValidator.validateAndGet(emailVerificationToken);

        verificationCode.setUsed(true);

        var user = verificationCode.getUser();
        user.setEnabled(true);
    }

}
