package com.deliverif.app.models.algorithms;

import com.deliverif.app.models.map.DeliveryTour;
import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;

@Getter
public class GreedyAlgorithm implements AbstractSearchOptimalTourAlgorithm {
    public void optimize(DeliveryTour deliveryTour){
        throw new NotImplementedException();
    }
}
