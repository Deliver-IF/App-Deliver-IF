package com.deliverif.app.model;

import javafx.util.Pair;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class CityMap {
    private final Intersection warehouse;
    private final Set<Segment> segments;
    private final Set<Intersection> intersections;
    private final Map<Pair<String, String>, Segment> segmentsMap;
    private final Map<String, Set<String>> connections;
    private final Map<Integer, DeliveryTour> deliveryTours;
    private final Float minLatitude;
    private final Float maxLatitude;
    private final Float minLongitude;
    private final Float maxLongitude;
    private final Float latitudeRange;
    private final Float longitudeRange;

    public CityMap(Intersection warehouse, Set<Segment> segments, Set<Intersection> intersections, Map<Pair<String, String>, Segment> segmentsMap, Map<String, Set<String>> connections, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        this.warehouse = warehouse;
        this.segments = segments;
        this.intersections = intersections;
        this.segmentsMap = segmentsMap;
        this.connections = connections;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;

        this.latitudeRange = maxLatitude - minLatitude;
        this.longitudeRange = maxLongitude - minLongitude;
        this.deliveryTours = new HashMap<>();
    }

    public void addDeliveryTour() {
        DeliveryTour deliveryTour = new DeliveryTour(this);
        deliveryTours.put(deliveryTour.getIdCourier(), deliveryTour);
    }

    public void addDeliveryRequest(int idCourier, Intersection destination) {
        DeliveryRequest deliveryRequest = new DeliveryRequest(10, destination);
        deliveryTours.get(idCourier).addDeliveryRequest(deliveryRequest);
    }
}
