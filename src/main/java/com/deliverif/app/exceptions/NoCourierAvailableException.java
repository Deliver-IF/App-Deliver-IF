package com.deliverif.app.exceptions;

public class NoCourierAvailableException extends Exception {
    public NoCourierAvailableException() {
        super("It is not possible to delete a courier. All couriers are assigned to at least one delivery.");
    }
}