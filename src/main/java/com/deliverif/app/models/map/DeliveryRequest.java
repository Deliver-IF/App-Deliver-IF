package com.deliverif.app.models.map;

import lombok.Getter;

@Getter
public class DeliveryRequest {
    private final int DELIVERY_TIME = 5;
    private static int idCounter = 0;
    private final int id;
    private final int startTimeWindow;
    private Intersection intersection;

    protected DeliveryRequest(int startTimeWindow) {
        this.startTimeWindow = startTimeWindow;
        this.id = idCounter++;
    }
}
