package com.deliverif.app;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.services.MapFactory;
import com.deliverif.app.vues.MainWindow;

import java.io.FileNotFoundException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        CityMap cm;
        try {
            URL res = Main.class.getClassLoader().getResource("maps/largeMap.xml");
            assert res != null;
            cm = MapFactory.createMapFromPathFile(res.getPath());
            new MainWindow(cm);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}