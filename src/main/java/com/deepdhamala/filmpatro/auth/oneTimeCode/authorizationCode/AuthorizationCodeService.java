package com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodePurpose;
import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeService;
import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeType;
import com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode.EmailVerificationCode;
import com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode.EmailVerificationCodeEntity;
import com.deepdhamala.filmpatro.auth.token.TokenManager;
import com.deepdhamala.filmpatro.auth.userAuthentication.UserAuthenticationResponseDto;
import com.deepdhamala.filmpatro.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationCodeService implements OneTimeCodeService<AuthorizationCode> {

    private final AuthorizationCodeRepository authorizationCodeRepository;
    private final TokenManager tokenManager;
    private final AuthorizationCodeValidator authorizationCodeValidator;


    @Override
    public AuthorizationCode generate(User user) {

        String code = UUID.randomUUID().toString();

        Instant expiresAt = Instant.now().plusSeconds(15); // 15 seconds

        return AuthorizationCode.builder()
                .user(user)
                .code(code)
                .type(OneTimeCodeType.UUID)
                .purpose(OneTimeCodePurpose.EMAIL_VERIFICATION)
                .expiresAt(expiresAt)
                .build();
    }

    @Override
    @Transactional
    public AuthorizationCode save(AuthorizationCode oneTimeCode) {

        AuthorizationCodeEntity emailVerificationCodeEntity = AuthorizationCodeEntity.builder()
                .authorizationCode(oneTimeCode.getCode())
                .codeType(oneTimeCode.getType())
                .user(oneTimeCode.getUser())
                .expiresAt(oneTimeCode.getExpiresAt())
                .build();

        authorizationCodeRepository.save(emailVerificationCodeEntity);

        return oneTimeCode;
    }

    @Override
    @Transactional
    public AuthorizationCode issue(User user) {
        AuthorizationCode oneTimeCode = generate(user);
        return save(oneTimeCode);
    }

    @Transactional
    public UserAuthenticationResponseDto exchangeAuthCodeForTokens(@Valid AuthorizationCodeForTokenDto authorizationCodeForTokenDto) {

        String authCode = authorizationCodeForTokenDto.getAuthorizationCode();
        AuthorizationCodeEntity authorizationCode = authorizationCodeValidator.validateAndGet(authCode);

        authorizationCode.setUsed(true);

        return tokenManager.issueTokens(authorizationCode.getUser());
    }

}
