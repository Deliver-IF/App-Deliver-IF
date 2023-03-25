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

    private DeliveryService() {
    }

    public static DeliveryService getInstance() {
        if (instance == null) {
            instance = new DeliveryService();
        }
        return instance;
    }

    public void searchOptimalDeliveryTour(DeliveryTour deliveryTour) {
        NaiveAlgorithm.getInstance().optimize(deliveryTour);
    }

    public DeliveryTour loadDeliveryFromFile(String filePath) {
        throw new NotImplementedException();
    }

    public void saveDeliveryToFile(String filePath, DeliveryTour deliveryTour) {
        throw new NotImplementedException();
    }

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
