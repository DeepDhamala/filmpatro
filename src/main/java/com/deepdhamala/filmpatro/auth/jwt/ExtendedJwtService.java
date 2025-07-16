package com.deepdhamala.filmpatro.auth.jwt;

import com.deepdhamala.filmpatro.auth.token.Token;
import com.deepdhamala.filmpatro.auth.token.Tokens;

/**
 * Extended JWT service interface for generating and managing JWT access and refresh tokens.
 * <p>
 * Provides methods to generate individual access or refresh tokens, as well as both tokens together.
 */
public interface ExtendedJwtService{

    /**
     * Generates a refresh token based on the provided JWT token request.
     *
     * @param jwtTokenRequest the request containing user and context information for token generation
     * @return a {@link Token} representing the generated refresh token
     */
    Token generateRefreshToken(JwtTokenRequest jwtTokenRequest);

    /**
     * Generates an access token based on the provided JWT token request.
     *
     * @param jwtTokenRequest the request containing user and context information for token generation
     * @return a {@link Token} representing the generated access token
     */
    Token generateAccessToken(JwtTokenRequest jwtTokenRequest);

    /**
     * Generates both access and refresh tokens based on the provided JWT token request.
     *
     * @return a {@link Token} representing both access and refresh tokens
     */
    Tokens generateTokens(JwtTokenRequest jwtTokenRequest);

}
