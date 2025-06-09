package com.deepdhamala.filmpatro.user.security;

import com.deepdhamala.filmpatro.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}