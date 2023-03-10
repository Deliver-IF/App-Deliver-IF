package com.deliverif.app.vues;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.utils.Constants;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private JPanel main;
    private Map map;

    public MainWindow(CityMap map) {
        super();

        this.setTitle(Constants.APP_NAME);
        this.setContentPane(this.main);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(800, 700);

        this.map.putClientProperty("map", map);
    }
}
