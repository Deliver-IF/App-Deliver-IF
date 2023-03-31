package com.deliverif.app.algorithm;

import com.deliverif.app.exceptions.WrongDeliveryTimeException;
import com.deliverif.app.model.CityMap;
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
import java.util.HashMap;
import java.util.Map;

public class GreedyAlgorithmTest {
    private CityMap cityMap;
    private Map<String, Intersection> mappedIntersections;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        URL res = NaiveAlgorithmTest.class.getResource("GreedyAlgorithmOptimizeMap.xml");
        assert res != null;
        cityMap = MapFactory.createMapFromFile(new File(URLDecoder.decode(res.getPath(), StandardCharsets.UTF_8)));
        cityMap.addDeliveryTour(0, "Toto", true);
        cityMap.addDeliveryTour(true);
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
        cityMap.getDeliveryTours().get(0).addDeliveryRequest(9, mappedIntersections.get("3"));
        cityMap.getDeliveryTours().get(0).addDeliveryRequest(8, mappedIntersections.get("4"));
        DeliveryTour deliveryTour = cityMap.getDeliveryTours().get(0);
        try {
            GreedyAlgorithm.getInstance().optimize(deliveryTour);
        } catch (WrongDeliveryTimeException e) {
            assert false;
        }
        assert deliveryTour.getTour().size() == 4;
        assert deliveryTour.getTour().get(0).getOrigin().getId().equals("1");
        assert deliveryTour.getTour().get(0).getDestination().getId().equals("4");
        assert deliveryTour.getTour().get(1).getOrigin().getId().equals("4");
        assert deliveryTour.getTour().get(1).getDestination().getId().equals("2");
        assert deliveryTour.getTour().get(2).getOrigin().getId().equals("2");
        assert deliveryTour.getTour().get(2).getDestination().getId().equals("3");
        assert deliveryTour.getTour().get(3).getOrigin().getId().equals("3");
        assert deliveryTour.getTour().get(3).getDestination().getId().equals("1");

        assert deliveryTour.getStops().get(0).getArrivalTime() == 8*60+7;
        assert deliveryTour.getStops().get(1).getArrivalTime() == 9*60;
    }

    @Test
    public void testOptimizeWithTooLongPath() {
        cityMap.getDeliveryTours().get(0).addDeliveryRequest(8, mappedIntersections.get("6"));
        DeliveryTour deliveryTour = cityMap.getDeliveryTours().get(0);
        try {
            GreedyAlgorithm.getInstance().optimize(deliveryTour);
            assert false;
        } catch (WrongDeliveryTimeException e) {
            assert true;
        }
    }
}
