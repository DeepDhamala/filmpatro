package com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode;

import com.deepdhamala.filmpatro.auth.oneTimeCode.OneTimeCodeType;
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
public class AuthorizationCodeService {

    private final AuthorizationCodeRepository authorizationCodeRepository;
    private final TokenManager tokenManager;
    private final AuthorizationCodeValidator authorizationCodeValidator;

    @Transactional
    public String issueAuthorizationCode(User user) {

        String authCode = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(20);

        AuthorizationCode authorizationCode = AuthorizationCode.builder()
                .authorizationCode(authCode)
                .user(user)
                .expiresAt(expiresAt)
                .codeType(OneTimeCodeType.UUID)
                .build();

        authorizationCodeRepository.save(authorizationCode);

        return authCode;

    }

    @Transactional
    public UserAuthenticationResponseDto exchangeAuthCodeForTokens(@Valid AuthorizationCodeForTokenDto authorizationCodeForTokenDto) {

        String authCode = authorizationCodeForTokenDto.getAuthorizationCode();
        AuthorizationCode authorizationCode = authorizationCodeValidator.validateAndGet(authCode);

        authorizationCode.setUsed(true);

        return tokenManager.issueTokens(authorizationCode.getUser());
    }

}
