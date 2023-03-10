package com.deliverif.app.models.map;

import lombok.Getter;

import java.util.List;

@Getter
public class CityMap {
    private final Intersection warehouse;
    private final List<Street> streets;


    private CityMap(Intersection warehouse, List<Street> streets) {
        this.warehouse = warehouse;
        this.streets = streets;
    }


    public static CityMap create(Intersection warehouse, List<Street> streets) {
        return new CityMap(warehouse, streets);
    }
}
