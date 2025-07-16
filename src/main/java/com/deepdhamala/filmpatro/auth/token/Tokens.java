package com.deepdhamala.filmpatro.auth.token;

import com.deepdhamala.filmpatro.auth.token.accessToken.AccessToken;
import com.deepdhamala.filmpatro.auth.token.refreshToken.RefreshToken;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tokens {

    private Token accessToken;
    private Token refreshToken;

}
