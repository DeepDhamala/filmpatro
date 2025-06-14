package com.deepdhamala.filmpatro.user.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthCodeForTokensDto {
    @NotBlank
    private String authorizationCode;
}
