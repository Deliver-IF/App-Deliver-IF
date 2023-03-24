package com.deliverif.app.model;

import lombok.Getter;

@Getter
public class DeliveryRequest {
    private final int DELIVERY_TIME = 5;
    private static int idCounter = 0;
    private final int id;
    private final int startTimeWindow;
    private final Intersection intersection;

    protected DeliveryRequest(int startTimeWindow, Intersection intersection) {
        this.startTimeWindow = startTimeWindow;
        this.id = idCounter++;
        this.intersection = intersection;
    }

    public static void resetIdCounter() {
        idCounter = 0;
    }
}
