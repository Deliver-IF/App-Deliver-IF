package com.deliverif.app.services;

import com.deliverif.app.algorithm.NaiveAlgorithm;
import com.deliverif.app.model.DeliveryTour;
import org.apache.commons.lang3.NotImplementedException;

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

}
