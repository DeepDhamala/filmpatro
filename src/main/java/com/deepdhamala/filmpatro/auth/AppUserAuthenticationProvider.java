package com.deepdhamala.filmpatro.auth;

import com.deepdhamala.filmpatro.user.UserRepository;
import com.deepdhamala.filmpatro.user.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUserAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));
        var principalUser = UserPrincipal.builder().user(user).build();
        if(principalUser.getPassword() == null || !passwordEncoder.matches(rawPassword, principalUser.getPassword())) {
            throw new UsernameNotFoundException("Invalid credentials for user: " + email);
        }
        return new UsernamePasswordAuthenticationToken(principalUser.getUsername(), principalUser.getPassword(), principalUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
