package com.deepdhamala.filmpatro.auth.oneTimeCode.otp;

public class InvalidOtpException extends RuntimeException{

    public InvalidOtpException(String message) {
        super(message);
    }

    public InvalidOtpException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOtpException(Throwable cause) {
        super(cause);
    }
}
