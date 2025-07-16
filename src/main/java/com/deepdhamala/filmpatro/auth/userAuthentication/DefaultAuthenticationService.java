package com.deepdhamala.filmpatro.auth.userAuthentication;

import com.deepdhamala.filmpatro.auth.jwt.IJwtService;
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
public class DefaultAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenManager tokenManager;
    private final IJwtService iJwtService;
    private final JwtService jwtService;

//    public AccessRefreshTokenResponseDto authenticate(@Valid AuthenticationRequestDto authenticationRequestDto){
//        Tokens tokens = jwtService2.is
//        Arrays.sort((a,b)->);
//    }



    @Deprecated
    public AccessRefreshTokenResponseDto authenticateUser(@Valid AuthenticationRequestDto authenticationRequestDto) {

        authenticateCredentials(authenticationRequestDto);

        User user = userService.getByEmailOrThrow(authenticationRequestDto.getEmail());

        return tokenManager.issueTokens(user);

    }

    public AccessRefreshTokenResponseDto refreshTokenForTokens(@Valid RequestTokensByRefreshTokenDto requestTokensByRefreshTokenDto) {

        String userEmail = iJwtService.fullValidateAndExtractUsername(requestTokensByRefreshTokenDto.getRefreshToken());
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
