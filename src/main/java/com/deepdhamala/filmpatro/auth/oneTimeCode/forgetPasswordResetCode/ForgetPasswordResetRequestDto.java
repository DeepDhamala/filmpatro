package com.deepdhamala.filmpatro.auth.oneTimeCode.forgetPasswordResetCode;

import com.deepdhamala.filmpatro.auth.password.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@PasswordMatches
public class ForgetPasswordResetRequestDto {

    @NotBlank
    private String forgetPasswordResetCode;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

}
