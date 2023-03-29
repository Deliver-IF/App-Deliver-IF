package com.deliverif.app.algorithm;

import com.deliverif.app.model.DeliveryTour;

public interface AbstractSearchOptimalTourAlgorithm {
    public void optimize(DeliveryTour deliveryTour);
}
