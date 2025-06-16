package com.deepdhamala.filmpatro.auth.oauth2;

import com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode.AuthorizationCodeService;
import com.deepdhamala.filmpatro.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthorizationCodeService authorizationCodeService;
    private final OAuth2UserService oauth2UserService;

    @Value("${app.oauth2.redirect-url}")
    private String redirectUrlBase;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        var attributes = new Oauth2UserAttributes(
                oAuth2User.getAttribute("email"),
                oAuth2User.getAttribute("name"),
                oAuth2User.getAttribute("picture")
        );

        User user = oauth2UserService.getOrCreateUser(attributes.email(), attributes.fullName(), attributes.avatarUrl());

        String authCode = authorizationCodeService.issueAuthorizationCode(user);

        String redirectUrl = redirectUrlBase + "?authcode=" + authCode;
        response.sendRedirect(redirectUrl);
    }
}


