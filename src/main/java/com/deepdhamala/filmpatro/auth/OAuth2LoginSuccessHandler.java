package com.deepdhamala.filmpatro.auth;

import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.UserRepository;
import com.deepdhamala.filmpatro.user.security.Role;
import com.deepdhamala.filmpatro.user.security.UserAuthenticationResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");
        String avatarUrl = oAuth2User.getAttribute("picture");

        if(email == null || fullName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email or username not provided by OAuth2 provider.");
            return;
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if(userOptional.isEmpty()){
//            Register the user if they do not exist
            var baseUsernameFromEmail = email.split("@")[0];
            String usernameToUse = baseUsernameFromEmail;
            int counter = 1;
            while(userRepository.existsByUsername(usernameToUse)){
                usernameToUse = baseUsernameFromEmail + counter++;
            }
            user = User.builder()
                    .email(email)
                    .fullName(fullName)
                    .avatarUrl(avatarUrl)
                    .username(usernameToUse)
                    .password(null)
                    .role(Role.USER)
                    .build();
            user = userRepository.save(user);
        }
        else {
//            Get the existing user if it exists
            user = userOptional.get();
        }
        var userPrincipal = com.deepdhamala.filmpatro.user.security.UserPrincipal.builder()
                .user(user)
                .build();
        String accessToken = jwtService.generateUserResourceAccessToken(userPrincipal);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);

        String authCode = jwtService.saveUserTokenWithAuthCode(user, accessToken, refreshToken).getAuthCodeForTokens();

        String redirectUrl = "http://localhost:5500/?authcode="+ authCode;
        response.sendRedirect(redirectUrl);
    }
}


