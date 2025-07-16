package com.deepdhamala.filmpatro.auth.token;

import com.deepdhamala.filmpatro.auth.jwt.IJwtService;
import com.deepdhamala.filmpatro.auth.jwt.JwtService;
import com.deepdhamala.filmpatro.auth.token.accessToken.AccessTokenService;
import com.deepdhamala.filmpatro.auth.token.refreshToken.RefreshTokenService;
import com.deepdhamala.filmpatro.auth.userAuthentication.AccessRefreshTokenResponseDto;
import com.deepdhamala.filmpatro.auth.principalUser.UserPrincipal;
import com.deepdhamala.filmpatro.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenManager {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final IJwtService jwtService;
    private final JwtService jwtService2;

    public Tokens saveAccessAndRefreshTokens(Tokens tokens) {
        accessTokenService.saveToken(tokens.getAccessToken());
        refreshTokenService.saveRefreshToken(tokens.getRefreshToken());
        return tokens;
    }


    /**
     * @deprecated Use {@link #saveAccessAndRefreshTokens(Tokens)} instead.
     */
    @Deprecated
    public void saveAccessAndRefreshTokens(User user, String accessToken, String refreshToken) {
        accessTokenService.saveAccessToken(user, accessToken);
        refreshTokenService.saveRefreshToken(user, refreshToken);
    }

//    public Tokens issueTokens(UserDetails userDetails) {
//        jwtService2.generateTokens
//    }

    @Deprecated
    public AccessRefreshTokenResponseDto issueTokens(User user) {

        UserPrincipal userPrincipal = UserPrincipal.builder().user(user).build();

        String jwtToken = jwtService.generateUserResourceAccessToken(userPrincipal);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);

        saveAccessAndRefreshTokens(user, jwtToken, refreshToken);

        return AccessRefreshTokenResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void revokeAllTokensByUser(User user) {
        accessTokenService.revokeAllAccessTokenByUser(user);
        refreshTokenService.revokeAllRefreshTokenByUser(user);
    }

    public void revokeToken(String accessToken, String refreshToken) {
        accessTokenService.revokeAccessToken(accessToken);
        refreshTokenService.revokeRefreshToken(refreshToken);
    }
}
