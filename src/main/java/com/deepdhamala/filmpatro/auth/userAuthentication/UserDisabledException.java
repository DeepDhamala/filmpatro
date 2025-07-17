package com.deepdhamala.filmpatro.auth.userAuthentication;

public class UserDisabledException extends RuntimeException {
    public UserDisabledException(String message) {
        super(message);
    }
}
