package com.deliverif.app.models.algorithms;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.DeliveryTour;
import com.deliverif.app.models.map.Intersection;
import com.deliverif.app.services.MapFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NaiveAlgorithmTest {

    private CityMap cityMap;
    private Map<String, Intersection> mappedIntersections;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        URL res = NaiveAlgorithmTest.class.getResource("NaiveAlgorithmOptimizeMap.xml");
        assert res != null;
        cityMap = MapFactory.createMapFromPathFile(URLDecoder.decode(res.getPath(), StandardCharsets.UTF_8));
        cityMap.addDeliveryTour();
        mappedIntersections = new HashMap<>();
        for (Intersection intersection : cityMap.getIntersections()) {
            mappedIntersections.put(intersection.getId(), intersection);
        }
    }
    @AfterEach
    public void tearDown() {
        cityMap = null;
        DeliveryTour.resetIdCounter();
    }
    @Test
    public void testOptimize() {
        NaiveAlgorithm naiveAlgorithm = new NaiveAlgorithm();
        cityMap.addDeliveryRequest(0, mappedIntersections.get("3"));
        cityMap.addDeliveryRequest(0, mappedIntersections.get("4"));
        DeliveryTour deliveryTour = cityMap.getDeliveryTours().get(0);
        naiveAlgorithm.optimize(deliveryTour);

    }

    @Test
    public void testAssignDeliveryToTour() {

    }
}
