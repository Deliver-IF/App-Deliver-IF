package com.deliverif.app.model;

import lombok.Getter;

import java.util.Random;

/**
 * Represents an order made by a client.
 */
@Getter
public class DeliveryRequest {
    /**
     * Time needed by a courier to realize a delivery once they arrived at the specified intersection.
     */
    private final int DELIVERY_TIME = 5;

    /**
     * The id of the delivery request.
     */
    private final int id;

    /**
     * The minimum time from which the delivery can be realized.
     */
    private final int startTimeWindow;

    /**
     * The intersection where the delivery has to be made.
     */
    private final Intersection intersection;

    public DeliveryRequest(int startTimeWindow, Intersection intersection) {
        this.startTimeWindow = startTimeWindow;
        this.id = new Random().nextInt(1000000000);
        this.intersection = intersection;
    }

    protected DeliveryRequest(int idRequest, int startTimeWindow, Intersection intersection) {
        this.startTimeWindow = startTimeWindow;
        this.id = idRequest;
        this.intersection = intersection;
    }
}
