package com.deliverif.app.algorithm;

import com.deliverif.app.model.CityMap;
import com.deliverif.app.model.DataModel;
import com.deliverif.app.model.DeliveryTour;
import com.deliverif.app.model.Intersection;
import com.deliverif.app.services.MapFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NaiveAlgorithmTest {

    private CityMap cityMap;
    private Map<String, Intersection> mappedIntersections;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        URL res = NaiveAlgorithmTest.class.getResource("NaiveAlgorithmOptimizeMap.xml");
        assert res != null;
        cityMap = MapFactory.createMapFromFile(new File(URLDecoder.decode(res.getPath(), StandardCharsets.UTF_8)));
        cityMap.addDeliveryTour(0,"Toto");
        cityMap.addDeliveryTour();
        mappedIntersections = new HashMap<>();
        for (Intersection intersection : cityMap.getIntersections().values()) {
            mappedIntersections.put(intersection.getId(), intersection);
        }
    }
    @AfterEach
    public void tearDown() {
        cityMap = null;
    }
    @Test
    public void testOptimize() {
        // cityMap.getDeliveryTours().values().stream().toList().get(0)  => id = 0
        cityMap.getDeliveryTours().values().stream().toList().get(0).addDeliveryRequest(9, mappedIntersections.get("3"));
        cityMap.getDeliveryTours().values().stream().toList().get(0).addDeliveryRequest(10, mappedIntersections.get("4"));
        DeliveryTour deliveryTour = cityMap.getDeliveryTours().values().stream().toList().get(0);
        NaiveAlgorithm.getInstance().optimize(deliveryTour);
        assert deliveryTour.getTour().size() == 4;

        ArrayList<String> extremities = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>();

        extremities.add(deliveryTour.getTour().get(0).getOrigin().getId());
        extremities.add(deliveryTour.getTour().get(0).getDestination().getId());
        expected.add("1"); expected.add("3");
        assert extremities.containsAll(expected);
        extremities.clear(); expected.clear();

        extremities.add(deliveryTour.getTour().get(1).getOrigin().getId());
        extremities.add(deliveryTour.getTour().get(1).getDestination().getId());
        expected.add("3"); expected.add("2");
        assert extremities.containsAll(expected);
        extremities.clear(); expected.clear();

        extremities.add(deliveryTour.getTour().get(2).getOrigin().getId());
        extremities.add(deliveryTour.getTour().get(2).getDestination().getId());
        expected.add("2"); expected.add("4");
        assert extremities.containsAll(expected);
        extremities.clear(); expected.clear();

        extremities.add(deliveryTour.getTour().get(3).getOrigin().getId());
        extremities.add(deliveryTour.getTour().get(3).getDestination().getId());
        expected.add("4"); expected.add("1");
        assert extremities.containsAll(expected);
        extremities.clear(); expected.clear();
    }

    @Test
    public void testAssignDeliveryToTour() {

    }
}
