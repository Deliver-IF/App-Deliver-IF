package com.deliverif.app.model;

import lombok.Getter;
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
    public Segment(String name, Float length, Intersection origin, Intersection destination) {
        this.name = name;
        this.length = length;
        this.origin = origin;
        if (!this.origin.getReachableIntersections().containsKey(destination)) {
            this.origin.getReachableIntersections().put(destination, this);
        }
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

    public Segment reverse() {
        return new Segment(this.name, this.length, this.destination, this.origin);
    }

    @Override
    public int hashCode() {
        return this.origin.hashCode() ^ this.destination.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Segment other)) {
            return false;
        }
        return this.origin.equals(other.origin) && this.destination.equals(other.destination);
    }

    public Intersection getEndIntersection(Intersection startIntersection) {
        if (startIntersection == origin) {
            return destination;
        } else if (startIntersection == destination) {
            return origin;
        } else {
            return null;
        }
    }
}