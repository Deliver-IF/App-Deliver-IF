package com.deliverif.app.models.map;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;

@Getter
public class Street {
    private final String name;
    private final Float length;
    private final Intersection origin;
    private final Intersection destination;

    protected Street(String name, Float length, Intersection origin, Intersection destination) {
        this.name = name;
        this.length = length;
        this.origin = origin;
        this.origin.getReachableIntersections().add(Pair.of(destination, this));
        this.destination = destination;
        this.destination.getReachableIntersections().add(Pair.of(origin, this));
    }

    public static Street create(Node nStreet, HashMap<String, Intersection> intersections) throws Exception {
        if (nStreet == null || nStreet.getNodeType() != Node.ELEMENT_NODE) {
            throw new Exception("Invalid node");
        }

        Element element = (Element) nStreet;
        Intersection origin = intersections.get(element.getAttribute("origin"));
        if (origin == null) {
            throw new Exception("Invalid origin");
        }
        Intersection destination = intersections.get(element.getAttribute("destination"));
        if (destination == null) {
            throw new Exception("Invalid destination");
        }

        return new Street(
                element.getAttribute("name"),
                Float.parseFloat(element.getAttribute("length")),
                origin,
                destination
        );
    }
}