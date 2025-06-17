package com.deepdhamala.filmpatro.auth.userAuthentication;

import com.deepdhamala.filmpatro.auth.jwt.JwtService;
import com.deepdhamala.filmpatro.auth.token.TokenManager;
import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenManager tokenManager;
    private final JwtService jwtService;

    public UserAuthenticationResponseDto authenticateUser(@Valid AuthenticationRequestDto authenticationRequestDto) {

        authenticateCredentials(authenticationRequestDto);

        User user = userService.getByEmailOrThrow(authenticationRequestDto.getEmail());

        return tokenManager.issueTokens(user);

    }

    public UserAuthenticationResponseDto refreshTokenForTokens(@Valid RequestTokensByRefreshTokenDto requestTokensByRefreshTokenDto) {

        String userEmail = jwtService.fullValidateAndExtractUsername(requestTokensByRefreshTokenDto.getRefreshToken());
        User user = userService.getByEmailOrThrow(userEmail);

        return tokenManager.issueTokens(user);
    }


    private void authenticateCredentials(AuthenticationRequestDto authenticationRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDto.getEmail(),
                        authenticationRequestDto.getPassword()
                )
        );
    }
}
