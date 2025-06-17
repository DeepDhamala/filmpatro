package com.deepdhamala.filmpatro.auth.jwt.exception;

public class JwtBaseException extends RuntimeException{
    public JwtBaseException(String message) {
        super(message);
    }

    public JwtBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtBaseException(Throwable cause) {
        super(cause);
    }
}
