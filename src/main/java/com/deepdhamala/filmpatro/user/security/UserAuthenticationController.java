package com.deepdhamala.filmpatro.user.security;

import com.deepdhamala.filmpatro.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth/user")
@RequiredArgsConstructor
public class UserAuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserAuthenticationResponseDto> registerUser(
            @RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {
        return ResponseEntity.ok(authenticationService.userRegistration(userRegisterRequestDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponseDto> authenticate(
            @RequestBody @Valid AuthenticationRequestDto authenticationRequestDto
    ) {
        return ResponseEntity.ok(authenticationService.userAuthentication(authenticationRequestDto));
    }

    @PostMapping("/exchange-authcode-for-tokens")
    public ResponseEntity<UserAuthenticationResponseDto> exchangeAuthCodeForTokens(
            @RequestBody @Valid AuthCodeForTokensDto authCodeForTokensDto) {
        return ResponseEntity.ok(authenticationService.exchangeAuthCodeForTokens(authCodeForTokensDto));
    }

    @GetMapping("/google/login")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

}