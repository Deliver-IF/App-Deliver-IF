package com.deliverif.app.model;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a tour to deliver all the requested content and which is done by a specific courier.
 */
@Getter
public class DeliveryTour {
    /**
     * Id of the last created courier.
     */
    private static int idCounter = 1;

    /**
     * The id of the courier realizing the delivery tour.
     */
    private final int idCourier;

    /**
     * The CityMap object associated whith the deliviery tour.
     */
    private final CityMap cityMap;

    /**
     * List of all the delivery requests the courier has to realize.
     */
    private ArrayList<DeliveryRequest> stops;

    /**
     * List of all the segments the courier has to follow to go through every single delivery request's intersection
     * they have to stop at.
     */
    private ArrayList<Segment> tour;

    /**
     * The javafx objects drawn on the map pane to display the delivery tour.
     */
    private Collection<Shape> shapes;

    // TODO : switch to protected
    public DeliveryTour(CityMap cityMap) {
        this.idCourier = idCounter++;
        this.cityMap = cityMap;
        this.stops = new ArrayList<>();
        this.tour = new ArrayList<>();
        this.shapes = new ArrayList<>();
    }

    /**
     * Add a new delivery request to a delivery tour.
     *
     * @param deliveryRequest   the new delivery request to add to the delivery tour.
     */
    public void addDeliveryRequest(DeliveryRequest deliveryRequest) {
        stops.add(deliveryRequest);
    }

    /**
     * Remove the delivery request from the delivery tour.
     *
     * @param deliveryRequest   the new delivery request to remove to the delivery tour.
     */
    public void removeDeliveryRequest(DeliveryRequest deliveryRequest) {
        stops.remove(deliveryRequest);
    }

    // TODO : remove method
    public void addTour(Segment segment) {
        tour.add(segment);
    }

    /**
     * Reset the counter of delivery tours back to 0.
     */
    public static void resetIdCounter() {
        idCounter = 0;
    } // Todo : why is it not 1 like variable declaration ?

    public void addShape(Shape shape) {
        shapes.add(shape);
    }
}
