package com.deepdhamala.filmpatro.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    public CustomDaoAuthenticationProvider(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) {

        String rawPassword = (authentication.getCredentials() != null) ? authentication.getCredentials().toString() : "";

        if (rawPassword.isBlank()) {
            throw new BadCredentialsException("Password cannot be blank");
        }

        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}