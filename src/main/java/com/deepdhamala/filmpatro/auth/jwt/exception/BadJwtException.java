package com.deepdhamala.filmpatro.auth.jwt.exception;

public class BadJwtException extends JwtBaseException {

    public BadJwtException(String message){
        super(message);
    }

    public  BadJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
