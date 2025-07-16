package com.deepdhamala.filmpatro.auth.userAuthentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RequiredArgsConstructor
public abstract class AbstractUserAuthenticationService<R extends AuthRequest, S> implements UserAuthenticationService<R, S>{

    private final AuthenticationManager authenticationManager;

    protected void authenticateCredentials(UsernamePasswordRequestDto authenticationRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDto.getEmail(),
                        authenticationRequestDto.getPassword()
                )
        );
    }
}
