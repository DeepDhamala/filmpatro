package com.deepdhamala.filmpatro.auth.userAuthentication;

import com.deepdhamala.filmpatro.auth.jwt.JwtService;
import com.deepdhamala.filmpatro.auth.jwt.JwtTokenRequest;
import com.deepdhamala.filmpatro.auth.token.Tokens;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class UserNamePasswordToTokenAuthenticationService extends AbstractUserAuthenticationService<UsernamePasswordRequestDto, AccessRefreshTokenResponseDto> implements JwtUserAuthenticationService {

    private final JwtService jwtService;


    public UserNamePasswordToTokenAuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    public AccessRefreshTokenResponseDto authenticate(@Valid UsernamePasswordRequestDto request) {
        authenticateCredentials(request);
        Tokens tokens = jwtService.generateTokens(JwtTokenRequest.builder().subject(request.getEmail()).build());
        return AccessRefreshTokenResponseDto.builder()
                .accessToken(tokens.getAccessToken().getToken())
                .refreshToken(tokens.getRefreshToken().getToken())
                .build();
    }
}
