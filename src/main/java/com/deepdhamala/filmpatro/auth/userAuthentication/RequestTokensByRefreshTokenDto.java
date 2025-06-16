package com.deepdhamala.filmpatro.auth.userAuthentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestTokensByRefreshTokenDto {
    @NotBlank
    private String refreshToken;
}
