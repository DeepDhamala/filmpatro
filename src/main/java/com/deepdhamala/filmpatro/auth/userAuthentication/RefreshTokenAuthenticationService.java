package com.deepdhamala.filmpatro.auth.userAuthentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenAuthenticationService extends AbstractUserAuthenticationService<RefreshTokenRequestDto, AccessRefreshTokenResponseDto>{

    public RefreshTokenAuthenticationService(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public AccessRefreshTokenResponseDto authenticate(RefreshTokenRequestDto request) {
        return null;
    }
}
