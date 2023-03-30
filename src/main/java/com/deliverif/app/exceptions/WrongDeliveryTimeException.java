package com.deliverif.app.exceptions;

public class WrongDeliveryTimeException extends Exception {
    public WrongDeliveryTimeException() {
        super("The delivery time after the time window.");
    }
}