package com.deliverif.app.models.map;

import com.deliverif.app.models.algorithms.astar.GraphNode;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Intersection implements GraphNode {
    private final String id;
    private final float longitude;
    private final float latitude;
    private final Set<Pair<Intersection, Segment>> reachableIntersections;

    // TODO : constructor has to be switch to protected
    public Intersection(String id, Float longitude, Float latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.reachableIntersections = new HashSet<>();
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
