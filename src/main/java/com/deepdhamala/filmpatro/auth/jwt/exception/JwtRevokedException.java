package com.deepdhamala.filmpatro.auth.jwt.exception;

public class JwtRevokedException extends JwtBaseException {

    public JwtRevokedException(String message) {
        super(message);
    }

    public JwtRevokedException(String message, Throwable cause) {
        super(message, cause);
    }
}
