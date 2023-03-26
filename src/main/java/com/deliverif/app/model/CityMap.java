package com.deliverif.app.model;

import javafx.util.Pair;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class CityMap {
    private final Intersection warehouse;
    private final Set<Segment> streets;
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

    /**
     * @param warehouse     the location of the warehouse, represented by an Intersection object.
     * @param segments      a set of all the segments / streets that constitute the map.
     * @param intersections a set of all the intersections that constitute the map.
     * @param segmentsMap   a Map mapping an id of an intersection to the corresponding Intersection object.
     * @param connections   a Map mapping an id of an intersection to the ids of all the intersections accessible from
     *                      this intersection.
     * @param minLatitude   the minimum latitude accessible on the map.
     * @param maxLatitude   the maximum latitude accessible on the map.
     * @param minLongitude  the minimum longitude accessible on the map.
     * @param maxLongitude  the maximum longitude accessible on the map.
     */
    private CityMap(Intersection warehouse, Set<Segment> segments, Set<Intersection> intersections, Map<Pair<String, String>, Segment> segmentsMap, Map<String, Set<String>> connections, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        this.warehouse = warehouse;
        this.streets = segments;
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

    /**
     * Create a new CityMap object.
     *
     * @param warehouse     the location of the warehouse, represented by an Intersection object.
     * @param segments      a set of all the segments / streets that constitute the map.
     * @param intersections a set of all the intersections that constitute the map.
     * @param segmentsMap   a Map mapping an id of an intersection to the corresponding Intersection object.
     * @param connections   a Map mapping an id of an intersection to the ids of all the intersections accessible from
     *                      this intersection.
     * @param minLatitude   the minimum latitude accessible on the map.
     * @param maxLatitude   the maximum latitude accessible on the map.
     * @param minLongitude  the minimum longitude accessible on the map.
     * @param maxLongitude  the maximum longitude accessible on the map.
     * @return              the newly created CityMap object.
     */
    public static CityMap create(Intersection warehouse, Set<Segment> segments, Set<Intersection> intersections, Map<Pair<String, String>, Segment> segmentsMap, Map<String, Set<String>> connections, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        return new CityMap(warehouse, segments, intersections, segmentsMap, connections, minLatitude, maxLatitude, minLongitude, maxLongitude);
    }

    /**
     * Add a new DeliveryTour object to the current list of DeliveryTour objects.
     */
    public void addDeliveryTour() {
        DeliveryTour deliveryTour = new DeliveryTour(this);
        deliveryTours.put(deliveryTour.getIdCourier(), deliveryTour);
    }

    /**
     * Add a new DeliveryRequest object to a DeliveryTour object.
     *
     * @param idCourier     the id of the courier that has to manage the new delivery request.
     * @param destination   the intersection where the delivery request is located,
     *                      represented by an Intersection object.
     */
    public void addDeliveryRequest(int idCourier, Intersection destination) {
        DeliveryRequest deliveryRequest = new DeliveryRequest(10, destination);
        deliveryTours.get(idCourier).addDeliveryRequest(deliveryRequest);
    }
}
