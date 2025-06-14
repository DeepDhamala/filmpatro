package com.deepdhamala.filmpatro.auth.userAuth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthCodeForTokensDto {
    @NotBlank
    private String authorizationCode;
}
