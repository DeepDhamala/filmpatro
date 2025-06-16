package com.deepdhamala.filmpatro.auth.userRegistration;

import com.deepdhamala.filmpatro.auth.oneTimeCode.emailVerificationCode.EmailVerificationCodeService;
import com.deepdhamala.filmpatro.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/auth/user/registration")
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;
    private final EmailVerificationCodeService emailVerificationCodeService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {
        userRegistrationService.registerNewUser(userRegisterRequestDto);
        return ResponseEntity.ok(ApiResponse.success(null, "User Registration Successful!"));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String verificationCode) {
        emailVerificationCodeService.verifyRegisteredEmail(verificationCode);
        return ResponseEntity.ok(ApiResponse.success(null, "Email verification successful"));
    }
}
