package com.deliverif.app.models.map;

import lombok.Getter;

import java.util.List;

@Getter
public class CityMap {
    private final Intersection warehouse;
    private final List<Segment> segments;
    private final Float minLatitude;
    private final Float maxLatitude;
    private final Float minLongitude;
    private final Float maxLongitude;
    private final Float latitudeRange;
    private final Float longitudeRange;


    private CityMap(Intersection warehouse, List<Segment> segments, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        this.warehouse = warehouse;
        this.segments = segments;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.latitudeRange = maxLatitude - minLatitude;
        this.longitudeRange = maxLongitude - minLongitude;
    }


    public static CityMap create(Intersection warehouse, List<Segment> segments, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        return new CityMap(warehouse, segments, minLatitude, maxLatitude, minLongitude, maxLongitude);
    }
}
