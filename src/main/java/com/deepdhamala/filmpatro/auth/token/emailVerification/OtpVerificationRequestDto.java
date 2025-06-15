package com.deepdhamala.filmpatro.auth.token.emailVerification;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerificationRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otp;
}