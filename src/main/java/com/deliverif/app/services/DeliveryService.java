package com.deliverif.app.services;

import com.deliverif.app.algorithm.AntColonyAlgorithm;
import com.deliverif.app.exceptions.WrongDeliveryTimeException;
import com.deliverif.app.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;
import java.util.ArrayList;
import java.util.Map;

public class DeliveryService {
    private static DeliveryService instance;

    private DeliveryService() {}

    /**
     * Get an instance of the DeliveryService.
     *
     * @return  the DeliveryService object.
     */
    public static DeliveryService getInstance() {
        if (instance == null) {
            instance = new DeliveryService();
        }
        return instance;
    }

    /**
     * Optimize the delivery tour to minimize the time spent to stop at every delivery request points.
     *
     * @param deliveryTour  the DeliveryTour object to optimize.
     */
    public void searchOptimalDeliveryTour(DeliveryTour deliveryTour) throws WrongDeliveryTimeException {
        AntColonyAlgorithm.getInstance().optimize(deliveryTour);
    }

    public void loadDeliveriesFromFile(File file, CityMap cityMap) throws FileNotFoundException {
        // load the delivery tours from a xml file
        if (!file.exists()) {
            throw new FileNotFoundException(file.getPath());
        }

        // clean the city map
        cityMap.getDeliveryTours().clear();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nDeliveries = doc.getElementsByTagName("deliveries");
            if (nDeliveries.getLength() != 1) {
                throw new Exception("Invalid number of deliveries");
            }

            Element deliveries = (Element) nDeliveries.item(0);
            NodeList deliveryTours = deliveries.getElementsByTagName("delivery-tour");

            for(int iTour = 0; iTour < deliveryTours.getLength(); iTour++) {
                Element deliveryTourElement = (Element) deliveryTours.item(iTour);
                int idCourierDeliveryTour = Integer.parseInt(deliveryTourElement.getAttribute("id"));
                String nameCourierDeliveryTour = deliveryTourElement.getAttribute("name");
                DeliveryTour newDeliveryTour = cityMap.addDeliveryTour(idCourierDeliveryTour, nameCourierDeliveryTour);

                NodeList nRequestsParent = deliveryTourElement.getElementsByTagName("requests");
                if (nRequestsParent.getLength() != 1) {
                    throw new Exception("Invalid number of requests node");
                }

                NodeList nSegmentsParent = deliveryTourElement.getElementsByTagName("segments");
                if (nSegmentsParent.getLength() != 1) {
                    throw new Exception("Invalid number of segments node");
                }

                NodeList requests = ((Element) nRequestsParent.item(0)).getElementsByTagName("request");
                for(int i = 0; i < requests.getLength(); i++) {
                    Element request = (Element) requests.item(i);
                    int idRequest = Integer.parseInt(request.getAttribute("id"));
                    int startTimeWindow = Integer.parseInt(request.getAttribute("startTimeWindow"));
                    String idIntersection = request.getAttribute("idIntersection");
                    newDeliveryTour.addDeliveryRequest(idRequest, startTimeWindow, cityMap.getIntersections().get(idIntersection));
                }

                NodeList segments = ((Element) nSegmentsParent.item(0)).getElementsByTagName("segment");
                for(int i = 0; i < segments.getLength(); i++) {
                    Element segment = (Element) segments.item(i);
                    String idIntersection1 = segment.getAttribute("idOrigin");
                    String idIntersection2 = segment.getAttribute("idDestination");
                    newDeliveryTour.addSegment(cityMap.getIntersections().get(idIntersection1), cityMap.getIntersections().get(idIntersection2));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDeliveriesToFile(File file, Collection<DeliveryTour> deliveryTours) throws FileAlreadyExistsException {
        // store the delivery tours in a xml file
        if (file.exists()) {
            throw new FileAlreadyExistsException(file.getPath());
        }


        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.newDocument();
            Element rootElement = document.createElement("deliveries");
            for(DeliveryTour deliveryTour : deliveryTours)
            {
                Element deliveryTourElement = document.createElement("delivery-tour");
                deliveryTourElement.setAttribute("id", String.valueOf(deliveryTour.getCourier().getIdCourier()));
                deliveryTourElement.setAttribute("name", deliveryTour.getCourier().getCourierName());
                Element deliveryRequestsElement = document.createElement("requests");
                for(DeliveryRequest deliveryRequest : deliveryTour.getStops())
                {
                    Element deliveryRequestElement = document.createElement("request");
                    deliveryRequestElement.setAttribute("id", String.valueOf(deliveryRequest.getId()));
                    deliveryRequestElement.setAttribute("startTimeWindow", String.valueOf(deliveryRequest.getStartTimeWindow()));
                    deliveryRequestElement.setAttribute("idIntersection", String.valueOf(deliveryRequest.getIntersection().getId()));
                    deliveryRequestsElement.appendChild(deliveryRequestElement);
                }
                deliveryTourElement.appendChild(deliveryRequestsElement);

                Element deliverySegmentsElement = document.createElement("segments");
                for(Segment segment : deliveryTour.getTour())
                {
                    Element deliverySegmentElement = document.createElement("segment");
                    deliverySegmentElement.setAttribute("idOrigin", String.valueOf(segment.getOrigin().getId()));
                    deliverySegmentElement.setAttribute("idDestination", String.valueOf(segment.getDestination().getId()));
                    deliverySegmentsElement.appendChild(deliverySegmentElement);
                }
                deliveryTourElement.appendChild(deliverySegmentsElement);
                rootElement.appendChild(deliveryTourElement);
            }
            document.appendChild(rootElement);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(document),
                        new StreamResult(new FileOutputStream(file)));


            } catch (TransformerException | IOException te) {
                te.printStackTrace();
            }

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all the DeliveryRequest objects that are located on a specific intersection.
     *
     * @param map           the map containing the data of the streets and intersections.
     * @param intersection  the intersection to get the delivery requests from.
     * @return              the list of DeliveryRequest objects located on the specified Intersection object.
     */
    public ArrayList<DeliveryRequest> getAllDeliveryRequestFromIntersection(CityMap map, Intersection intersection) {
        ArrayList<DeliveryRequest> currentDeliveryRequests = new ArrayList<>();
        Map<Integer, DeliveryTour> deliveriesTourMap = map.getDeliveryTours();
        // For each tours
        for (DeliveryTour deliveryTour : deliveriesTourMap.values()) {
            // For each stops
            for (DeliveryRequest deliveryRequest : deliveryTour.getStops()) {
                // If delivery request intersection is the current intersection
                if(deliveryRequest.getIntersection().getId().equals(intersection.getId())) {
                    currentDeliveryRequests.add(deliveryRequest);
                }
            }
        }
        return currentDeliveryRequests;
    }
}
