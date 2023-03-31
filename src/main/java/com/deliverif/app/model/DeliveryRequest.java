package com.deliverif.app.model;

import javafx.scene.shape.Circle;
import com.deliverif.app.exceptions.WrongDeliveryTimeException;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

/**
 * Represents an order made by a client.
 */
@Getter
public class DeliveryRequest {
    /**
     * Time needed by a courier to realize a delivery once they arrived at the specified intersection.
     */
    public static final int DELIVERY_TIME = 5;

    /**
     * The id of the delivery request.
     */
    private final int id;

    /**
     * The minimum time from which the delivery can be realized.
     */
    @Setter
    private int startTimeWindow;

    /**
     * The time at which the delivery will be realized in minutes.
     */
    private int arrivalTime;

    /**
     * The intersection where the delivery has to be made.
     */
    private final Intersection intersection;

    /**
     * The delivery tour the delivery request is part of.
     */
    @Setter
    private DeliveryTour deliveryTour;

    @Setter
    private Circle deliveryRequestCircle;


    public DeliveryRequest(int startTimeWindow, Intersection intersection, DeliveryTour deliveryTour) {
        this.startTimeWindow = startTimeWindow;
        this.id = new Random().nextInt(1000000000);
        this.intersection = intersection;
        this.deliveryTour = deliveryTour;
        this.deliveryRequestCircle =  new Circle();
    }

    protected DeliveryRequest(int idRequest, int startTimeWindow, Intersection intersection, DeliveryTour deliveryTour) {
        this.startTimeWindow = startTimeWindow;
        this.id = idRequest;
        this.intersection = intersection;
        this.deliveryTour = deliveryTour;
        this.deliveryRequestCircle = new Circle();
    }

    /**
     * Set the arrival time of the delivery request.
     *
     * @param arrivalTime  the arrival time of the delivery courier.
     * @throws WrongDeliveryTimeException
     */
    public void setArrivalTime(int arrivalTime) throws WrongDeliveryTimeException {
        if (arrivalTime < this.startTimeWindow*60) {
            arrivalTime = this.startTimeWindow*60;
        } else if (arrivalTime >= (this.startTimeWindow + 1)*60) {
            throw new WrongDeliveryTimeException();
        }
        this.arrivalTime = arrivalTime;
    }

    public float getDeliveryDuration() {
        return DELIVERY_TIME;
    }
}
