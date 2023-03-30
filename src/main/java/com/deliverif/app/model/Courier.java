package com.deliverif.app.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a courier.
 */
@Getter
@Setter
public class Courier {

    /**
     * The id of the courier realizing the delivery tour.
     */
    private final int idCourier;

    /**
     * The id of the courier realizing the delivery tour.
     */
    private String courierName;

    protected Courier() {
        this.idCourier = new Random().nextInt(1000000000);
        this.courierName = null;

    }

    protected Courier(String name) {
        this.idCourier = new Random().nextInt(1000000000);
        this.courierName = name;

    }

    protected Courier(int id, String name) {
        this.idCourier = id;
        this.courierName = name;

    }

    @Override
    public String toString() {
        return this.getCourierName();
    }
}
