package com.deliverif.app.model;

import com.deliverif.app.controller.MapController;
import com.deliverif.app.exceptions.NoConfiguredDeliveryException;
import com.deliverif.app.exceptions.WrongSelectedMapException;
import com.deliverif.app.services.DeliveryService;
import com.deliverif.app.services.MapFactory;
import lombok.Getter;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

public class DataModel {
    @Getter
    CityMap cityMap;
    @Getter
    MapController mapController;
    public DataModel() {
        this.cityMap = null;
        this.mapController = new MapController();
    }

    /**
     * Set a new CityMap object to the DataModel object.
     *
     * @param file                      the file containing the data of the new map to load.
     * @throws FileNotFoundException    the file is missing on the user's device.
     */
    public void loadMapFromFile(File file) throws FileNotFoundException {
        this.cityMap = MapFactory.createMapFromFile(file);
    }

    /**
     * Load a delivery tour from a file.
     *
     * @param file                      the file containing the data of the delivery tour to load.
     * @throws FileNotFoundException    the file is missing on the user's device.
     */
    public void loadTourFromFile(File file) throws FileNotFoundException, WrongSelectedMapException {
        DeliveryService.getInstance().loadDeliveriesFromFile(file, cityMap);
    }

    public void saveTourToFile(File file) throws FileAlreadyExistsException, NoConfiguredDeliveryException {
        DeliveryService.getInstance().saveDeliveriesToFile(file, cityMap.getDeliveryTours().values());
    }
}
