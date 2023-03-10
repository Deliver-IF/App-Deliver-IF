package com.deliverif.app.models.map;

import lombok.Getter;

import java.util.List;

@Getter
public class CityMap {
    private final Intersection warehouse;
    private final List<Street> streets;
    private final Float minLatitude;
    private final Float maxLatitude;
    private final Float minLongitude;
    private final Float maxLongitude;


    private CityMap(Intersection warehouse, List<Street> streets, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        this.warehouse = warehouse;
        this.streets = streets;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
    }


    public static CityMap create(Intersection warehouse, List<Street> streets, Float minLatitude, Float maxLatitude, Float minLongitude, Float maxLongitude) {
        return new CityMap(warehouse, streets, minLatitude, maxLatitude, minLongitude, maxLongitude);
    }
}
