package com.deliverif.app.models.map;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class CityMap {
    private final Intersection warehouse;
    private final ArrayList<Segment> segments;
    private ArrayList<DeliveryTour> deliveryTours;
    private final Float minLatitude;
    private final Float maxLatitude;
    private final Float minLongitude;
    private final Float maxLongitude;


    private CityMap(Intersection warehouse, ArrayList<Segment> segments, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        this.warehouse = warehouse;
        this.segments = segments;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
    }


    public static CityMap create(Intersection warehouse, ArrayList<Segment> segments, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        return new CityMap(warehouse, segments, minLatitude, maxLatitude, minLongitude, maxLongitude);
    }
}
