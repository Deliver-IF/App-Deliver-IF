package com.deliverif.app.exceptions;

public class WrongSelectedMapException extends Exception {
    public WrongSelectedMapException() {
        super("The map is not the same as the one used to generate the deliveries");
    }
}
