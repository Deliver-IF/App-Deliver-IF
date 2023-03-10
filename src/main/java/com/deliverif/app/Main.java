package com.deliverif.app;

import com.deliverif.app.vues.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        try {
            URL res = Main.class.getClassLoader().getResource("maps/smallMap.xml");
            assert res != null;
            CityMap cm = MapFactory.createMapFromPathFile(res.getPath());
            System.out.println(cm);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        JFrame main = new MainWindow();
    }
}