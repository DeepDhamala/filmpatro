package com.deepdhamala.filmpatro.auth.userAuth;

import com.deepdhamala.filmpatro.auth.AuthenticationService;
import com.deepdhamala.filmpatro.auth.token.emailVerification.EmailVerificationTokenService;
import com.deepdhamala.filmpatro.auth.token.emailVerification.OtpVerificationRequestDto;
import com.deepdhamala.filmpatro.common.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth/user")
@RequiredArgsConstructor
public class UserAuthenticationController {

    private final AuthenticationService authenticationService;
    private final EmailVerificationTokenService emailVerificationTokenService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(
            @RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {
        authenticationService.registerUser(userRegisterRequestDto);
        return ResponseEntity.ok(ApiResponse.success(null, "User Registration Successful!"));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<UserAuthenticationResponseDto>> authenticate(
            @RequestBody @Valid AuthenticationRequestDto authenticationRequestDto
    ) {
        UserAuthenticationResponseDto authResponse = authenticationService.userAuthentication(authenticationRequestDto);
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Authentication successful!"));
    }

    @PostMapping("/refreshtoken-for-tokens")
    public ResponseEntity<ApiResponse<UserAuthenticationResponseDto>> refreshTokenForTokens(
            @RequestBody @Valid RefreshTokenForTokensDto refreshTokenForTokensDto) {
        UserAuthenticationResponseDto refreshResponse = authenticationService.refreshTokenForTokens(refreshTokenForTokensDto);
        return ResponseEntity.ok(ApiResponse.success(refreshResponse, "Tokens refreshed successfully"));
    }


    @PostMapping("/exchange-authcode-for-tokens")
    public ResponseEntity<ApiResponse<UserAuthenticationResponseDto>> exchangeAuthCodeForTokens(
            @RequestBody @Valid AuthCodeForTokensDto authCodeForTokensDto) {
        UserAuthenticationResponseDto exchangeResponse = authenticationService.exchangeAuthCodeForTokens(authCodeForTokensDto);
        return ResponseEntity.ok(ApiResponse.success(exchangeResponse, "Tokens exchanged successfully"));
    }

    @GetMapping("/google/login")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<UserAuthenticationResponseDto>> verifyOtp(
            @RequestBody @Valid OtpVerificationRequestDto otpVerificationRequestDto) {
        UserAuthenticationResponseDto userAuthenticationResponseDto = authenticationService.verifyOtp(otpVerificationRequestDto);
        return ResponseEntity.ok(ApiResponse.success(userAuthenticationResponseDto, "OTP verification successful"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto,
            Principal loggedInUser) {
        authenticationService.changePassword(changePasswordRequestDto, loggedInUser);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequestDto forgotPasswordRequestDto) {
        authenticationService.recoverForgetPassword(forgotPasswordRequestDto);
        return ResponseEntity.ok(ApiResponse.success(null, "Forgot password request processed successfully"));
    }

    @PostMapping("/reset-forgot-password")
    public ResponseEntity<ApiResponse<String>> resetForgotPassword(
            @RequestBody @Valid ForgetPasswordResetRequestDto resetForgetPasswordRequestDto) {
        authenticationService.resetForgetPassword(resetForgetPasswordRequestDto);
        return ResponseEntity.ok(ApiResponse.success(null, "Forgot password reset successfully"));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
        emailVerificationTokenService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.success(null, "Email verification successful"));
    }
}