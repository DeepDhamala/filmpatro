package com.deepdhamala.filmpatro.audit;

import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.security.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditorAwareImpl {
    @Bean
    public AuditorAware<String> auditAware() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional::empty;
        }
        var user = (User) authentication.getPrincipal();
        return () -> Optional.ofNullable(user.getId()).map(String::valueOf);

    }
}
