package com.deepdhamala.filmpatro.auth;

import com.deepdhamala.filmpatro.auth.token.TokenManager;
import com.deepdhamala.filmpatro.auth.token.accessToken.AccessTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final AccessTokenRepository accessTokenRepository;
    private final TokenManager tokenManager;

    @Override
    @Transactional
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        final String jwt = extractJwt(request);
        final String refreshToken = extractRefreshToken(request);

        if (jwt == null || refreshToken == null) {
            return;
        }
        tokenManager.revokeToken(jwt, refreshToken);

        SecurityContextHolder.clearContext();
    }

    private String extractJwt(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private String extractRefreshToken(HttpServletRequest request) {
        final String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken != null && !refreshToken.isEmpty()) {
            return refreshToken;
        }
        return null;
    }
}