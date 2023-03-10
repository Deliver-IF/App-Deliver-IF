package com.deliverif.app;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.services.MapFactory;
import com.deliverif.app.vues.MainWindow;
import javax.swing.*;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        // Load from file
        CityMap map = MapFactory.createMapFromPathFile("/Users/benchai/Downloads/fichiersXML2022/smallMap.xml");

        JFrame main = new MainWindow(map);

    }
}