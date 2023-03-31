package com.deliverif.app.services;

import com.deliverif.app.exceptions.NoConfiguredDeliveryException;
import com.deliverif.app.exceptions.WrongSelectedMapException;
import com.deliverif.app.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class TestableDeliveryTour extends DeliveryTour {
    protected TestableDeliveryTour(CityMap cityMap, int idCourier, String nameCourier, boolean visible) {
        super(cityMap, idCourier, nameCourier, visible);
    }
}
class TestableIntersection extends Intersection {
    public TestableIntersection(String id) {
        super(id, 0f, 0f);
    }
}
class TestableDeliveryRequest extends DeliveryRequest {
    protected TestableDeliveryRequest(int idRequest, int startTimeWindow, int arrivalTime, String idIntersection, DeliveryTour deliveryTour) {
        super(idRequest, startTimeWindow, arrivalTime, new TestableIntersection(idIntersection), deliveryTour);
    }
}
class TestableSegment extends Segment {
    public TestableSegment(String idOrigin, String idDestination) {
        super("name", 0f, new TestableIntersection(idOrigin), new TestableIntersection(idDestination));
    }
}

public class DeliveryServiceTest {
    private static final String DELIVERY_RESULT_FILE_PATH = DeliveryServiceTest.class.getResource("").getPath().concat("DeliveryService/result/deliveries.xml");
    @BeforeEach
    public void setUp() {
        File file = new File(DELIVERY_RESULT_FILE_PATH);
        if (file.exists()) {
            boolean res = file.delete();
            if (!res) {
                throw new RuntimeException("Could not delete file " + DELIVERY_RESULT_FILE_PATH);
            }
        }
    }
    @Test
    public void testLoadDeliveriesFromFile() throws FileNotFoundException, WrongSelectedMapException {
        String inputFilePath = DeliveryServiceTest.class.getResource("").getPath().concat("DeliveryService/input/loadDeliveriesFromFile.xml");
        String mapFilePath = DeliveryServiceTest.class.getResource("").getPath().concat("DeliveryService/input/testMap.xml");
        CityMap cityMap = MapFactory.createMapFromFile(new File(mapFilePath));
        DeliveryService.getInstance().loadDeliveriesFromFile(new File(inputFilePath), cityMap);

        assert String.valueOf(cityMap.hashCode()).equals("161406");
        assert cityMap.getDeliveryTours().size() == 2;

        DeliveryTour deliveryTour = cityMap.getDeliveryTours().get(0);
        assert deliveryTour.getCourier().getIdCourier() == 0;
        assert deliveryTour.getCourier().getCourierName().equals("toto");
        assert deliveryTour.getStops().size() == 3;
        assert deliveryTour.getStops().get(0).getId() == 0;
        assert deliveryTour.getStops().get(0).getStartTimeWindow() == 10;
        assert deliveryTour.getStops().get(0).getIntersection().getId().equals("5");
        assert deliveryTour.getStops().get(1).getId() == 1;
        assert deliveryTour.getStops().get(1).getStartTimeWindow() == 9;
        assert deliveryTour.getStops().get(1).getIntersection().getId().equals("6");
        assert deliveryTour.getStops().get(2).getId() == 2;
        assert deliveryTour.getStops().get(2).getStartTimeWindow() == 11;
        assert deliveryTour.getStops().get(2).getIntersection().getId().equals("6");

        assert deliveryTour.getTour().size() == 7;
        assert deliveryTour.getTour().get(0).getOrigin().getId().equals("1");
        assert deliveryTour.getTour().get(0).getDestination().getId().equals("3");
        assert deliveryTour.getTour().get(1).getOrigin().getId().equals("3");
        assert deliveryTour.getTour().get(1).getDestination().getId().equals("9");
        assert deliveryTour.getTour().get(2).getOrigin().getId().equals("9");
        assert deliveryTour.getTour().get(2).getDestination().getId().equals("6");
        assert deliveryTour.getTour().get(3).getOrigin().getId().equals("6");
        assert deliveryTour.getTour().get(3).getDestination().getId().equals("5");
        assert deliveryTour.getTour().get(4).getOrigin().getId().equals("5");
        assert deliveryTour.getTour().get(4).getDestination().getId().equals("6");
        assert deliveryTour.getTour().get(5).getOrigin().getId().equals("6");
        assert deliveryTour.getTour().get(5).getDestination().getId().equals("4");
        assert deliveryTour.getTour().get(6).getOrigin().getId().equals("4");
        assert deliveryTour.getTour().get(6).getDestination().getId().equals("1");

        deliveryTour = cityMap.getDeliveryTours().get(23);
        assert deliveryTour.getCourier().getIdCourier() == 23;
        assert deliveryTour.getCourier().getCourierName().equals("titi");
        assert deliveryTour.getStops().size() == 1;
        assert deliveryTour.getStops().get(0).getId() == 12;
        assert deliveryTour.getStops().get(0).getStartTimeWindow() == 9;
        assert deliveryTour.getStops().get(0).getIntersection().getId().equals("2");

        assert deliveryTour.getTour().size() == 2;
        assert deliveryTour.getTour().get(0).getOrigin().getId().equals("1");
        assert deliveryTour.getTour().get(0).getDestination().getId().equals("2");
        assert deliveryTour.getTour().get(1).getOrigin().getId().equals("2");
        assert deliveryTour.getTour().get(1).getDestination().getId().equals("1");
    }

    @Test
    public void testLoadDeliveriesFromFileWrongMap() throws FileNotFoundException {
        String inputFilePath = DeliveryServiceTest.class.getResource("").getPath().concat("DeliveryService/input/loadDeliveriesFromFile.xml");
        String mapFilePath = DeliveryServiceTest.class.getResource("").getPath().concat("DeliveryService/input/testWrongMap.xml");
        CityMap cityMap = MapFactory.createMapFromFile(new File(mapFilePath));
        try{
            DeliveryService.getInstance().loadDeliveriesFromFile(new File(inputFilePath), cityMap);
            assert false;
        } catch (WrongSelectedMapException e) {
            assert true;
        }
    }

    @Test
    public void testSaveDeliveriesToFile() throws FileAlreadyExistsException, NoConfiguredDeliveryException, FileNotFoundException {
        String expectedFilePath = DeliveryServiceTest.class.getResource("").getPath().concat("DeliveryService/expected/saveDeliveriesToFile.xml");
        String mapFilePath = DeliveryServiceTest.class.getResource("").getPath().concat("DeliveryService/input/testMap.xml");
        List<DeliveryTour> deliveryTours = new ArrayList<>();

        TestableDeliveryTour deliveryTour = new TestableDeliveryTour(MapFactory.createMapFromFile(new File(mapFilePath)), 0, "toto", true);
        deliveryTour.getStops().add(new TestableDeliveryRequest(0, 10, 600, "5", deliveryTour));
        deliveryTour.getStops().add(new TestableDeliveryRequest(1, 9, 540, "6", deliveryTour));
        deliveryTour.getStops().add(new TestableDeliveryRequest(2, 11, 660, "6", deliveryTour));
        deliveryTour.getTour().add(new TestableSegment("1", "3"));
        deliveryTour.getTour().add(new TestableSegment("3", "9"));
        deliveryTour.getTour().add(new TestableSegment("9", "6"));
        deliveryTour.getTour().add(new TestableSegment("6", "5"));
        deliveryTour.getTour().add(new TestableSegment("5", "6"));
        deliveryTour.getTour().add(new TestableSegment("6", "4"));
        deliveryTour.getTour().add(new TestableSegment("4", "1"));
        deliveryTours.add(deliveryTour);

        TestableDeliveryTour deliveryTour2 = new TestableDeliveryTour(null, 23, "titi", true);
        deliveryTour2.getStops().add(new TestableDeliveryRequest(12, 9, 540, "2", deliveryTour2));
        deliveryTour2.getTour().add(new TestableSegment("1", "2"));
        deliveryTour2.getTour().add(new TestableSegment("2", "1"));
        deliveryTours.add(deliveryTour2);

        DeliveryService deliveryService = DeliveryService.getInstance();
        deliveryService.saveDeliveriesToFile(new File(DELIVERY_RESULT_FILE_PATH), deliveryTours);
        //XMLUnit.setIgnoreWhitespace(true);
        // Compare the generated XML to the expected one
        Input.Builder i1 = Input.fromFile(DELIVERY_RESULT_FILE_PATH);
        Input.Builder i2 = Input.fromFile(expectedFilePath);
        XmlAssert.assertThat(i1).and(i2).ignoreWhitespace().areSimilar();
    }
}
