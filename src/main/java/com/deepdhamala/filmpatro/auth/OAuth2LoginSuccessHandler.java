package com.deepdhamala.filmpatro.auth;

import com.deepdhamala.filmpatro.auth.userAuth.UserPrincipal;
import com.deepdhamala.filmpatro.user.User;
import com.deepdhamala.filmpatro.user.UserRepository;
import com.deepdhamala.filmpatro.auth.userAuth.Role;
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
                    .enabled(true)
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
            if(!user.isEnabled()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Its look like you have active registration pending with this email. Please verify your email through opt first. Hint: Try to login with email and password then otp will be sent to your email, verify with that and  from next on wards you can follow this authentication");
                return;
            }
            if (!user.isAccountNonLocked() || !user.isCredentialsNonExpired() || !user.isAccountNonExpired()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Account not in a valid state.");
                return;
            }

        }
        var userPrincipal = UserPrincipal.builder()
                .user(user)
                .build();
        String accessToken = jwtService.generateUserResourceAccessToken(userPrincipal);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);

        String authCode = jwtService.saveUserTokenWithAuthCode(user, accessToken, refreshToken).getAuthCodeForTokens();

        String redirectUrl = "http://localhost:5500/?authcode="+ authCode;
        response.sendRedirect(redirectUrl);
    }
}


