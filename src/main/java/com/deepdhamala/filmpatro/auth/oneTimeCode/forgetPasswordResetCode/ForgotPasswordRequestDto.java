package com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordRequestDto {
    @NotBlank
    @Email
    private String email;
}
