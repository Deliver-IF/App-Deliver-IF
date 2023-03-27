package com.deliverif.app.model;

import javafx.scene.shape.Circle;
import com.deliverif.app.algorithm.astar.GraphNode;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a common point between multiple segments / streets.
 */
@Getter
public class Intersection implements GraphNode {
    /**
     * The id of the intersection.
     */
    private final String id;

    /**
     * The longitude at which the intersection is located.
     */
    private final float longitude;

    /**
     * The latitude at which the intersection is located.
     */
    private final float latitude;

    /**
     * A set of pairs of the intersections accessible from the intersection and the corresponding segments
     * leading to them.
     */
    private final Set<Pair<Intersection, Segment>> reachableIntersections;

    /**
     * The default shape that is used to represent an intersection on a map pane.
     */
    private Circle defaultShapeOnMap;

    public Intersection(String id, Float longitude, Float latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.reachableIntersections = new HashSet<>();
        this.defaultShapeOnMap = new Circle();
    }

    /**
     * Change the default shape that is used to represent an intersection on a map pane.
     *
     * @param point the new reference shape.
     */
    public void setDefaultShapeOnMap(Circle point) {
        this.defaultShapeOnMap = point;
    }

    /**
     * Create a new Intersection object.
     *
     * @param node  the Node object representing the intersection.
     * @return      the newly created Intersection object.
     * @throws Exception
     */
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
