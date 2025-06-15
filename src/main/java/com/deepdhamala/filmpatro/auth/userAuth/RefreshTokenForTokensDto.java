package com.deepdhamala.filmpatro.auth.userAuth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenForTokensDto {
    @NotBlank
    private String refreshToken;
}
