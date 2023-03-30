package com.deliverif.app.algorithm;

import com.deliverif.app.exceptions.WrongDeliveryTimeException;
import com.deliverif.app.model.DeliveryTour;

abstract public class AbstractSearchOptimalTourAlgorithm {
    abstract public void optimize(DeliveryTour deliveryTour) throws WrongDeliveryTimeException;

    public static float timeTaken(float length, float speed) {
        return length / speed;
    }
}
