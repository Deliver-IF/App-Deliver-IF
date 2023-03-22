package com.deliverif.app.services;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.Intersection;
import com.deliverif.app.models.map.Segment;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MapFactory {

    public static CityMap createMapFromPathFile(String pathFile) throws FileNotFoundException {
        File file = new File(pathFile);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        Float minLatitude = null, maxLatitude = null, minLongitude = null, maxLongitude = null;

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
            HashMap<String, Set<String>> connections = new HashMap<>();
            for (int i = 0; i < nIntersections.getLength(); i++) {
                Intersection intersection = Intersection.create(nIntersections.item(i));
                if (minLatitude == null || intersection.getLatitude() < minLatitude) {
                    minLatitude = intersection.getLatitude();
                }
                if (maxLatitude == null || intersection.getLatitude() > maxLatitude) {
                    maxLatitude = intersection.getLatitude();
                }
                if (minLongitude == null || intersection.getLongitude() < minLongitude) {
                    minLongitude = intersection.getLongitude();
                }
                if (maxLongitude == null || intersection.getLongitude() > maxLongitude) {
                    maxLongitude = intersection.getLongitude();
                }
                intersections.put(intersection.getId(), intersection);
                connections.put(intersection.getId(), new HashSet<>());
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
            Set<Segment> segments = new HashSet<>();
            Map<Pair<String, String>, Segment> segmentsMap = new HashMap<>();
            for (int i = 0; i < nStreets.getLength(); i++) {
                Segment segment = Segment.create(nStreets.item(i), intersections);
                segments.add(segment);
                connections.get(segment.getOrigin().getId()).add(segment.getDestination().getId());
                segmentsMap.put(new Pair<>(segment.getOrigin().getId(), segment.getDestination().getId()), segment);
            }

            return CityMap.create(warehouse, segments, new HashSet<>(intersections.values()), segmentsMap, connections, minLatitude, maxLatitude, minLongitude, maxLongitude);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
