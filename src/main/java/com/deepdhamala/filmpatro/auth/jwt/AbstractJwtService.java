package com.deepdhamala.filmpatro.auth.jwt;

import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Map;

@NoArgsConstructor
public abstract class AbstractJwtService implements JwtService2 {

    @Override
    public String generateToken(JwtTokenRequest jwtTokenRequest) {
        return generateTokenInternal(jwtTokenRequest);
    }

    @Override
    public <T> T extractClaim(String token, String claimName, Class<T> claimType) {
        return null;
    }


    protected String generateRefreshToken(JwtTokenRequest jwtTokenRequest){
//        jwtTokenRequest.("refresh");
        return generateToken(jwtTokenRequest);
    }

    protected abstract String generateTokenInternal(JwtTokenRequest jwtTokenRequest);

}
