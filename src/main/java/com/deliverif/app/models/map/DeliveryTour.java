package com.deliverif.app.models.map;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class DeliveryTour {
    private static int idCounter = 0;
    private final int idCourier;
    private ArrayList<DeliveryRequest> stops;
    private ArrayList<Segment> tour;

    protected DeliveryTour() {
        this.idCourier = idCounter++;
    }
}
