package com.deepdhamala.filmpatro.auth.userAuthentication;

import com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode.AuthorizationCodeForTokenDto;
import com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode.AuthorizationCodeService;
import com.deepdhamala.filmpatro.common.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v2/auth/user/authentication")
@RequiredArgsConstructor
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;
    private final AuthorizationCodeService authorizationCodeService;

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<UserAuthenticationResponseDto>> authenticateUser(
            @RequestBody @Valid AuthenticationRequestDto authenticationRequestDto) {
        var authResponse = userAuthenticationService.authenticateUser(authenticationRequestDto);
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Authentication successful!"));
    }

    @PostMapping("/refreshtoken-for-tokens")
    public ResponseEntity<ApiResponse<UserAuthenticationResponseDto>> refreshTokenForTokens(
            @RequestBody @Valid RequestTokensByRefreshTokenDto requestTokensByRefreshTokenDto) {
        var refreshResponse = userAuthenticationService.refreshTokenForTokens(requestTokensByRefreshTokenDto);
        return ResponseEntity.ok(ApiResponse.success(refreshResponse, "Tokens refreshed successfully"));
    }

    @PostMapping("/authcode-for-tokens")
    public ResponseEntity<ApiResponse<UserAuthenticationResponseDto>> exchangeAuthCodeForTokens(
            @RequestBody @Valid AuthorizationCodeForTokenDto authorizationCodeForTokenDto) {
        UserAuthenticationResponseDto exchangeResponse = authorizationCodeService.exchangeAuthCodeForTokens(authorizationCodeForTokenDto);
        return ResponseEntity.ok(ApiResponse.success(exchangeResponse, "Tokens exchanged successfully"));
    }

    @GetMapping("/google/login")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

}
