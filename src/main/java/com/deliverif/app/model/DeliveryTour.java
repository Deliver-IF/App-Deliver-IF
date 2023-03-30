package com.deliverif.app.model;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
/**
 * Represents a tour to deliver all the requested content and which is done by a specific courier.
 */
@Getter
public class DeliveryTour {
    /**
     * The id of the courier realizing the delivery tour.
     */
    private Courier courier;

    /**
     * The CityMap object associated whith the deliviery tour.
     */
    private final CityMap cityMap;

    /**
     * List of all the delivery requests the courier has to realize.
     */
    private final ArrayList<DeliveryRequest> stops;

    /**
     * List of all the segments the courier has to follow to go through every single delivery request's intersection
     * they have to stop at.
     */
    private final ArrayList<Segment> tour;

    /**
     * The javafx objects drawn on the map pane to display the delivery tour.
     */
    private final Collection<Shape> shapes;

    /**
     * Wether the delivery should be visible or not on the map pane.
     */
    private boolean visible;

    protected DeliveryTour(CityMap cityMap, boolean visible) {
        this.courier = new Courier();
        this.cityMap = cityMap;
        this.stops = new ArrayList<>();
        this.tour = new ArrayList<>();
        this.shapes = new ArrayList<>();
        this.visible = visible;
    }

    protected DeliveryTour(CityMap cityMap, Integer idCourier, String nameCourier, boolean visible) {
        this.cityMap = cityMap;
        this.courier = new Courier(idCourier,nameCourier);
        this.stops = new ArrayList<>();
        this.tour = new ArrayList<>();
        this.shapes = new ArrayList<>();
        this.visible = visible;
    }

    /**
     * Add a new delivery request to a delivery tour.
     *
     * @param deliveryRequest   the new delivery request to add to the delivery tour.
     */
    public void addDeliveryRequest(DeliveryRequest deliveryRequest) {
        stops.add(deliveryRequest);
    }

    public void addDeliveryRequest(int startTimeWindow, Intersection destination) {
        DeliveryRequest deliveryRequest = new DeliveryRequest(startTimeWindow, destination, this);
        stops.add(deliveryRequest);
    }

    public void addDeliveryRequest(int idRequest, int startTimeWindow, Intersection destination) {
        DeliveryRequest deliveryRequest = new DeliveryRequest(idRequest, startTimeWindow, destination, this);
        stops.add(deliveryRequest);
    }

    public void addSegment(Intersection origin, Intersection destination) {
        Segment segment = new Segment("default-name", 0f, origin, destination);
        tour.add(segment);
    }

    /**
     * Remove the delivery request from the delivery tour.
     *
     * @param deliveryRequest   the new delivery request to remove to the delivery tour.
     */
    public void removeDeliveryRequest(DeliveryRequest deliveryRequest) {
        stops.remove(deliveryRequest);
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }
}
