package com.deliverif.app.services;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.Intersection;
import com.deliverif.app.models.map.Street;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFactory {

    public static CityMap createMapFromPathFile(String pathFile) throws FileNotFoundException {
        File file = new File(pathFile);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nWarehouses = doc.getElementsByTagName("warehouse");
            if (nWarehouses.getLength() != 1) {
                throw new Exception("Invalid number of warehouses");
            }

            NodeList nIntersections = doc.getElementsByTagName("intersection");
            if (nIntersections.getLength() < 2) {
                throw new Exception("Invalid number of intersections");
            }
            HashMap<String, Intersection> intersections = new HashMap<>();
            for (int i = 0; i < nIntersections.getLength(); i++) {
                Intersection intersection = Intersection.create(nIntersections.item(i));
                intersections.put(intersection.getId(), intersection);
            }

            Element warehouseElement = (Element) nWarehouses.item(0);
            Intersection warehouse = intersections.get(warehouseElement.getAttribute("address"));
            if (warehouse == null) {
                throw new Exception("Invalid warehouse");
            }

            NodeList nStreets = doc.getElementsByTagName("segment");
            if (nStreets.getLength() < 1) {
                throw new Exception("Invalid number of segments");
            }
            List<Street> streets = new ArrayList<>();
            for (int i = 0; i < nStreets.getLength(); i++) {
                Street street = Street.create(nStreets.item(i), intersections);
                streets.add(street);
            }

            return CityMap.create(warehouse, streets);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
