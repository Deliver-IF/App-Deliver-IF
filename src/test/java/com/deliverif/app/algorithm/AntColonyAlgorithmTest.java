package com.deliverif.app.algorithm;

import com.deliverif.app.model.CityMap;
import com.deliverif.app.model.DeliveryTour;
import com.deliverif.app.model.Intersection;
import com.deliverif.app.model.Segment;
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

public class AntColonyAlgorithmTest {

    private CityMap cityMap;
    private Map<String, Intersection> mappedIntersections;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        URL res = AntColonyAlgorithmTest.class.getResource("NaiveAlgorithmOptimizeMap.xml");
        assert res != null;
        cityMap = MapFactory.createMapFromFile(new File(URLDecoder.decode(res.getPath(), StandardCharsets.UTF_8)));
        cityMap.addDeliveryTour(0);
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
        cityMap.getDeliveryTours().get(0).addDeliveryRequest(9, mappedIntersections.get("3"));
        cityMap.getDeliveryTours().get(0).addDeliveryRequest(10, mappedIntersections.get("4"));
        DeliveryTour deliveryTour = cityMap.getDeliveryTours().get(0);
        AntColonyAlgorithm.getInstance().optimize(deliveryTour);

        float length = deliveryTour.getTour().stream().map(Segment::getLength).reduce(0f, Float::sum);
        assert length == 239.09146f;

        assert deliveryTour.getTour().size() == 6;
        assert deliveryTour.getTour().get(0) == mappedIntersections.get("1").getSegmentTo(mappedIntersections.get("4"));
        assert deliveryTour.getTour().get(1) == mappedIntersections.get("4").getSegmentTo(mappedIntersections.get("2"));
        assert deliveryTour.getTour().get(2) == mappedIntersections.get("2").getSegmentTo(mappedIntersections.get("3"));
        assert deliveryTour.getTour().get(3) == mappedIntersections.get("3").getSegmentTo(mappedIntersections.get("2"));
        assert deliveryTour.getTour().get(4) == mappedIntersections.get("2").getSegmentTo(mappedIntersections.get("4"));
        assert deliveryTour.getTour().get(5) == mappedIntersections.get("4").getSegmentTo(mappedIntersections.get("1"));
    }
}
