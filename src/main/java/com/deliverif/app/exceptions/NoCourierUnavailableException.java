package com.deliverif.app.exceptions;

public class NoCourierUnavailableException extends Exception {
    public NoCourierUnavailableException() {
        super("It is not possible to delete a courier. All couriers are assigned to at least one delivery.");
    }
}