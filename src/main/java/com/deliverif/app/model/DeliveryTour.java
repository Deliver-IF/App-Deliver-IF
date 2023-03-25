package com.deliverif.app.model;

import lombok.Getter;
import java.util.ArrayList;

@Getter
public class DeliveryTour {
    private static int idCounter = 1;
    private final int idCourier;
    private final CityMap cityMap;
    private ArrayList<DeliveryRequest> stops;
    private ArrayList<Segment> tour;

    // TODO : switch to protected
    public DeliveryTour(CityMap cityMap) {
        this.idCourier = idCounter++;
        this.cityMap = cityMap;
        this.stops = new ArrayList<>();
        this.tour = new ArrayList<>();
    }

    public void addDeliveryRequest(DeliveryRequest deliveryRequest) {
        stops.add(deliveryRequest);
    }

    // TODO : remove method
    public void addTour(Segment segment) {
        tour.add(segment);
    }

    public static void resetIdCounter() {
        idCounter = 0;
    }
}
