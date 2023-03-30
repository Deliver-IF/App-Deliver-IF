package com.deliverif.app.model;

import com.deliverif.app.exceptions.NoCourierAvailableException;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class CityMap {
    private final Intersection warehouse;
    private final Set<Segment> streets;
    private final Map<String, Intersection> intersections;
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
     * @param connections   a Map mapping an id of an intersection to the ids of all the intersections accessible from
     *                      this intersection.
     * @param minLatitude   the minimum latitude accessible on the map.
     * @param maxLatitude   the maximum latitude accessible on the map.
     * @param minLongitude  the minimum longitude accessible on the map.
     * @param maxLongitude  the maximum longitude accessible on the map.
     */
    private CityMap(Intersection warehouse, Set<Segment> segments, Map<String, Intersection> intersections, Map<String, Set<String>> connections, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        this.warehouse = warehouse;
        this.streets = segments;
        this.intersections = intersections;
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
     * @param connections   a Map mapping an id of an intersection to the ids of all the intersections accessible from
     *                      this intersection.
     * @param minLatitude   the minimum latitude accessible on the map.
     * @param maxLatitude   the maximum latitude accessible on the map.
     * @param minLongitude  the minimum longitude accessible on the map.
     * @param maxLongitude  the maximum longitude accessible on the map.
     * @return              the newly created CityMap object.
     */
    public static CityMap create(Intersection warehouse, Set<Segment> segments, Map<String, Intersection> intersections, Map<String, Set<String>> connections, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        return new CityMap(warehouse, segments, intersections, connections, minLatitude, maxLatitude, minLongitude, maxLongitude);
    }

    /**
     * Add a new DeliveryTour object to the current list of DeliveryTour objects.
     */
    public DeliveryTour addDeliveryTour() {
        DeliveryTour deliveryTour = new DeliveryTour(this);
        deliveryTours.put(deliveryTour.getCourier().getIdCourier(), deliveryTour);
        return deliveryTour;
    }

    /**
     * Add a new DeliveryTour object to the current list of DeliveryTour objects.
     *
     * @param idCourier     the id of the courier that has to manage the new delivery tour.
     */
    public DeliveryTour addDeliveryTour(Integer idCourier, String nameCourier) {
        DeliveryTour deliveryTour = new DeliveryTour(this, idCourier, nameCourier);
        deliveryTours.put(deliveryTour.getCourier().getIdCourier(), deliveryTour);
        return deliveryTour;
    }

    /**
     * Delete a DeliveryTour object from the current list of DeliveryTour objects.
     *
     * @throws NoCourierAvailableException
     */
    public void deleteDeliveryTour() throws NoCourierAvailableException {
        boolean emptyDeliveryTour = false;
        for(Map.Entry<Integer, DeliveryTour> deliveryTourEntry : deliveryTours.entrySet()) {
            DeliveryTour currentDeliveryTour = deliveryTourEntry.getValue();
            if(currentDeliveryTour.getStops().size() == 0) {
                deliveryTours.remove(deliveryTourEntry.getKey(), deliveryTourEntry.getValue());
                emptyDeliveryTour = true;
                break;
            }
        }
        if(!emptyDeliveryTour) {
            throw new NoCourierAvailableException();
        }

    }

    /**
     * Add a new DeliveryRequest object to a DeliveryTour object.
     *
     * @param idCourier     the id of the courier that has to manage the new delivery request.
     * @param destination   the intersection where the delivery request is located,
     *                      represented by an Intersection object.
     */
    public void addDeliveryRequest(int idCourier, Intersection destination) {
        DeliveryTour deliveryTour = deliveryTours.get(idCourier);
        DeliveryRequest deliveryRequest = new DeliveryRequest(10, destination, deliveryTour);
        deliveryTour.addDeliveryRequest(deliveryRequest);
    }

    @Override
    public int hashCode() {
        return (warehouse != null ? warehouse.hashCode() : 1) * (streets != null ? streets.hashCode() : 1) ^
                (intersections != null ? intersections.hashCode() : 1) * (connections != null ? connections.hashCode() : 1);
    }
}
