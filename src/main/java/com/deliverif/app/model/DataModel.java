package com.deliverif.app.model;

import com.deliverif.app.services.MapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DataModel {
    CityMap cityMap;
    public DataModel() {
        this.cityMap = null;
    }

    public void loadMapFromFile(File mapFile) throws FileNotFoundException {
        this.cityMap = MapFactory.createMapFromPathFile(mapFile);
    }

}
