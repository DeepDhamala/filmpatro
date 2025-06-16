package com.deepdhamala.filmpatro.email;

public class EmailDeliveryException extends RuntimeException{
    public EmailDeliveryException(String message) {
        super(message);
    }

    public EmailDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailDeliveryException(Throwable cause) {
        super(cause);
    }
}
