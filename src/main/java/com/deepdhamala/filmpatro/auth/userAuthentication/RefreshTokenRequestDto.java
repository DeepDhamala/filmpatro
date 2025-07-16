package com.deepdhamala.filmpatro.auth.userAuthentication;

import com.deepdhamala.filmpatro.auth.token.Token;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenRequestDto implements AuthRequest{
    private Token refreshToken;
}
