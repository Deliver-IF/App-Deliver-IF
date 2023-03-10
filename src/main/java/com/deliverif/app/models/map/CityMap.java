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


    private CityMap(Intersection warehouse, List<Segment> segments, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        this.warehouse = warehouse;
        this.segments = segments;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
    }


    public static CityMap create(Intersection warehouse, List<Segment> segments, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        return new CityMap(warehouse, segments, minLatitude, maxLatitude, minLongitude, maxLongitude);
    }
}
