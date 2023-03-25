package com.deliverif.app.model;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;

/**
 * Represents a link between two intersections.
 * It can be a street or a portion of the delivery tour of a courier.
 */
@Getter
public class Segment {
    /**
     * The name of the intersection.
     */
    private final String name;

    /**
     * The length of the segment.
     */
    private final float length;

    /**
     * The start point of the Segment, represented by an Intersection.
     */
    private final Intersection origin;

    /**
     * The end point of the segment, represented by an Intersection.
     */
    private final Intersection destination;

    // TODO : constructor has to be switch to protected
    private Segment(String name, Float length, Intersection origin, Intersection destination) {
        this.name = name;
        this.length = length;
        this.origin = origin;
        this.origin.getReachableIntersections().add(Pair.of(destination, this));
        this.destination = destination;
    }

    /**
     * Create a new Segment object.
     *
     * @param nStreet       the Node representing the segment.
     * @param intersections the Map mapping an id of an intersection with corresponding Intersection object.
     * @return              the newly created Segment object.
     * @throws Exception
     */
    public static Segment create(Node nStreet, HashMap<String, Intersection> intersections) throws Exception {
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

        return new Segment(
                element.getAttribute("name"),
                Float.parseFloat(element.getAttribute("length")),
                origin,
                destination
        );
    }
}