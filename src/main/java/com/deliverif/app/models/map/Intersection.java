package com.deliverif.app.models.map;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;

@Getter
public class Intersection {
    private final String id;
    private final Float longitude;
    private final Float latitude;
    private final ArrayList<Pair<Intersection, Segment>> reachableIntersections;

    protected Intersection(String id, Float longitude, Float latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.reachableIntersections = new ArrayList<>();
    }


    public static Intersection create(Node node) throws Exception {
        if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
            throw new Exception("Invalid node");
        }

        Element element = (Element) node;

        return new Intersection(
                element.getAttribute("id"),
                Float.parseFloat(element.getAttribute("longitude")),
                Float.parseFloat(element.getAttribute("latitude"))
        );
    }
}
