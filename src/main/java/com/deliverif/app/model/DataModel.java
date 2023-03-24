package com.deliverif.app.model;

import com.deliverif.app.controller.MapController;
import com.deliverif.app.services.MapFactory;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;

public class DataModel {
    @Getter
    CityMap cityMap;
    @Getter
    MapController mapController;
    public DataModel() {
        this.cityMap = null;
        this.mapController = null;
    }

    public void loadMapFromFile(File mapFile) throws FileNotFoundException {
        this.cityMap = MapFactory.createMapFromPathFile(mapFile);
        mapController = new MapController();
    }

}
