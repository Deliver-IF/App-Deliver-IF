package com.deliverif.app.models.algorithms;

import com.deliverif.app.models.map.DeliveryTour;

public interface AbstractSearchOptimalTourAlgorithm {
    public void optimize(DeliveryTour deliveryTour);
}
