package com.deepdhamala.filmpatro.auth.token.refreshToken;

import com.deepdhamala.filmpatro.auth.jwt.IJwtService;
import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.TokenType;
import com.deepdhamala.filmpatro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final IJwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(Token refreshToken) {
        Date expiryDate = jwtService.extractExpiration(refreshToken.getToken());

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(refreshToken.getUser())
                .refreshToken(refreshToken.getToken())
                .expiresAt(expiryDate.toInstant())
                .tokenType(TokenType.BEARER)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }

    /**
     * @deprecated Use {@link #saveRefreshToken(Token)} instead.
     */
    @Deprecated
    public void saveRefreshToken(RefreshToken refreshToken) {
        Date expiryDate = jwtService.extractExpiration(refreshToken.getToken());

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(refreshToken.getUser())
                .refreshToken(refreshToken.getToken())
                .expiresAt(expiryDate.toInstant())
                .tokenType(TokenType.BEARER)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void saveRefreshToken(User user, String refreshToken) {

        Date expiryDate = jwtService.extractExpiration(refreshToken);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expiresAt(expiryDate.toInstant())
                .tokenType(TokenType.BEARER)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void revokeAllRefreshTokenByUser(User user) {

        refreshTokenRepository.findAllValidRefreshTokenByUser(user.getId())
                .forEach(refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                });

    }

    @Transactional
    public void revokeRefreshToken(String token) {
        refreshTokenRepository.findByRefreshToken((token)).ifPresent(storedToken -> {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            refreshTokenRepository.save(storedToken);
        });
    }
}
