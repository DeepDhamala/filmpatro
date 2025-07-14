package com.deepdhamala.filmpatro.auth.token.accessToken;

import com.deepdhamala.filmpatro.auth.jwt.JwtService;
import com.deepdhamala.filmpatro.auth.token.TokenType;
import com.deepdhamala.filmpatro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final JwtService jwtService;

    public void saveAccessToken(AccessToken accessToken) {

        Date expiryDate = jwtService.extractExpiration(accessToken.getAccessToken());

        AccessTokenEntity accessTokenEntity = AccessTokenEntity.builder()
                .user(accessToken.getUser())
                .accessToken(accessToken.getAccessToken())
                .expiresAt(expiryDate.toInstant())
                .tokenType(TokenType.BEARER)
                .build();

        accessTokenRepository.save(accessTokenEntity);
    }

    public void saveAccessToken(User user, String accessToken) {

        Date expiryDate = jwtService.extractExpiration(accessToken);

        AccessTokenEntity accessTokenEntity = AccessTokenEntity.builder()
                .user(user)
                .accessToken(accessToken)
                .expiresAt(expiryDate.toInstant())
                .tokenType(TokenType.BEARER)
                .build();

        accessTokenRepository.save(accessTokenEntity);
    }

    public void revokeAllAccessTokenByUser(User user) {
        accessTokenRepository.findAllValidAccessTokenByUser((user.getId()))
                .forEach(accessToken -> {
                    accessToken.setRevoked(true);
                    accessTokenRepository.save(accessToken);
                });
    }

    @Transactional
    public void revokeAccessToken(String token) {
        accessTokenRepository.findByAccessToken(token).ifPresent(storedToken -> {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            accessTokenRepository.save(storedToken);
        });
    }
}
