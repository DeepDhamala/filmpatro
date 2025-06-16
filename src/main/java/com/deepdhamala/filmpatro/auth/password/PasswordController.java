package com.deepdhamala.filmpatro.auth.password;

import com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode.ForgetPasswordResetCodeService;
import com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode.ForgetPasswordResetRequestDto;
import com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode.ForgotPasswordRequestDto;
import com.deepdhamala.filmpatro.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/auth/user/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;
    private final ForgetPasswordResetCodeService forgetPasswordResetCodeService;

    @PostMapping("/change")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto) {
        passwordService.changePassword(changePasswordRequestDto);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }

    @PostMapping("/forgot")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequestDto forgotPasswordRequestDto) {
        forgetPasswordResetCodeService.recoverForgetPassword(forgotPasswordRequestDto);
        return ResponseEntity.ok(ApiResponse.success(null, "Forgot password request processed successfully"));
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<String>> resetForgotPassword(
            @RequestBody @Valid ForgetPasswordResetRequestDto forgetPasswordResetCodeDto) {
        forgetPasswordResetCodeService.resetForgetPassword(forgetPasswordResetCodeDto);
        return ResponseEntity.ok(ApiResponse.success(null, "Forgot password reset successfully"));
    }
}
