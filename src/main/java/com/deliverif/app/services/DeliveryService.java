package com.deliverif.app.services;

import com.deliverif.app.algorithm.NaiveAlgorithm;
import com.deliverif.app.model.CityMap;
import com.deliverif.app.model.DeliveryRequest;
import com.deliverif.app.model.DeliveryTour;
import com.deliverif.app.model.Intersection;
import org.apache.commons.lang3.NotImplementedException;
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
    public void searchOptimalDeliveryTour(DeliveryTour deliveryTour) {
        NaiveAlgorithm.getInstance().optimize(deliveryTour);
    }

    public DeliveryTour loadDeliveryFromFile(String filePath) {
        throw new NotImplementedException();
    }

    public void saveDeliveryToFile(String filePath, DeliveryTour deliveryTour) {
        throw new NotImplementedException();
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
                if(deliveryRequest.getIntersection().getId() == intersection.getId()) {
                    currentDeliveryRequests.add(deliveryRequest);
                }
            }
        }
        return currentDeliveryRequests;
    }
}
