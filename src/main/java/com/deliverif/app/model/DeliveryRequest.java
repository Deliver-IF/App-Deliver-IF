package com.deliverif.app.model;

import lombok.Getter;

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
     * The id of the last created delivery request.
     */
    private static int idCounter = 0;

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
        this.id = idCounter++;
        this.intersection = intersection;
    }

    /**
     * Reset the counter of delivery requests back to its original value.
     * (Before the first delivery request was created)
     */
    public static void resetIdCounter() {
        idCounter = 0;
    }
}
