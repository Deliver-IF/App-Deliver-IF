package com.deliverif.app.models.map;

import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@Getter
public class Intersection {
    private final String id;
    private final Float longitude;
    private final Float latitude;

    protected Intersection(String id, Float longitude, Float latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
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
