package com.deepdhamala.filmpatro.auth.userAuth;

import com.deepdhamala.filmpatro.auth.AuthenticationService;
import com.deepdhamala.filmpatro.auth.otp.OtpVerificationRequestDto;
import com.deepdhamala.filmpatro.common.ApiResponse;
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
    public ResponseEntity<ApiResponse<String>> registerUser(
            @RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {
        authenticationService.userRegistration(userRegisterRequestDto);
        ApiResponse<String> response = ApiResponse.success(null, "User Registration Successful!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<UserAuthenticationResponseDto>> authenticate(
            @RequestBody @Valid AuthenticationRequestDto authenticationRequestDto
    ) {
        UserAuthenticationResponseDto authResponse = authenticationService.userAuthentication(authenticationRequestDto);
        ApiResponse<UserAuthenticationResponseDto> response = ApiResponse.success(authResponse, "Authentication successful");
        return ResponseEntity.ok(response);
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

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @RequestBody @Valid OtpVerificationRequestDto otpVerificationRequestDto) {
        authenticationService.verifyOtp(otpVerificationRequestDto);
        return ResponseEntity.ok("OTP verified successfully. Account activated.");
    }



}