package com.deepdhamala.filmpatro.auth.jwt.exception;

import io.jsonwebtoken.JwtException;

public class JwtAuthenticationException extends JwtException {
    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
