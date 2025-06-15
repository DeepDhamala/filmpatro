package com.deepdhamala.filmpatro.auth.userAuth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ForgetPasswordResetRequestDto {

    @NotBlank
    private String forgetPasswordResetToken;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmNewPassword;

}
