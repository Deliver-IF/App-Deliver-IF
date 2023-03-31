package com.deliverif.app.exceptions;

public class NoConfiguredDeliveryException extends Exception {
    public NoConfiguredDeliveryException() {
        super("No delivery has been configured.");
    }
}
