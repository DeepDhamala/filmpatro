package com.deepdhamala.filmpatro.auth.oneTimeCode.authorizationCode;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthorizationCodeForTokenDto {
    @NotBlank
    private String authorizationCode;
}
