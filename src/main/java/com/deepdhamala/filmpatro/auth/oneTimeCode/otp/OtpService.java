package com.deepdhamala.filmpatro.auth.oneTimeCode.otp;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodePurpose;
import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeService;
import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeType;
import com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode.AuthorizationCodeEntity;
import com.deepdhamala.filmpatro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OtpService implements OneTimeCodeService<Otp> {

    private static final SecureRandom random = new SecureRandom();

    private final OtpRepository otpRepository;

    @Override
    public Otp generate(User user) {
        Instant expiresAt = Instant.now().plusSeconds(2*60); // 120 seconds
        int otp = 100_000 + random.nextInt(900_000); // Generates a 6-digit number
        return Otp.builder()
                .code(String.valueOf(otp))
                .expiresAt(expiresAt)
                .type(OneTimeCodeType.UUID)
                .purpose(OneTimeCodePurpose.OTP)
                .user(user)
                .build();
    }

    @Override
    public Otp save(Otp oneTimeCode) {
        OtpEntity emailVerificationCodeEntity = OtpEntity.builder()
                .otp(oneTimeCode.getCode())
                .codeType(oneTimeCode.getType())
                .user(oneTimeCode.getUser())
                .expiresAt(oneTimeCode.getExpiresAt())
                .build();

        otpRepository.save(emailVerificationCodeEntity);

        return oneTimeCode;
    }

    @Override
    @Transactional
    public Otp issue(User user) {
        return save(generate(user));
    }

}
