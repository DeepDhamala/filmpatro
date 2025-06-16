package com.deepdhamala.filmpatro.auth.token.refreshToken;

import com.deepdhamala.filmpatro.auth.jwt.JwtService;
import com.deepdhamala.filmpatro.auth.token.TokenType;
import com.deepdhamala.filmpatro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(User user, String refreshToken) {

        Date expiryDate = jwtService.extractExpiration(refreshToken);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
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
