package com.deepdhamala.filmpatro.auth.oauth2;

public class Oauth2AttributeException extends RuntimeException {
    public Oauth2AttributeException(String message) {
        super(message);
    }

    public Oauth2AttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public Oauth2AttributeException(Throwable cause) {
        super(cause);
    }
}
