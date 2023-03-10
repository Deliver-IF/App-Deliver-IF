package com.deliverif.app.vues;

import com.deliverif.app.models.map.CityMap;

import javax.swing.*;
import java.awt.*;

public class Map extends JPanel {

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        CityMap map = (CityMap) this.getClientProperty("map");

        float deltaLongitude = map.getMaxLongitude() - map.getMinLongitude();
        float deltaLatitude = map.getMaxLatitude() - map.getMinLatitude();

        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(3));

        map.getSegments().forEach(street -> g.drawLine(
                (int) ((street.getOrigin().getLongitude() - map.getMinLongitude()) / deltaLongitude * this.getWidth()),
                (int) ((deltaLatitude - street.getOrigin().getLatitude() + map.getMinLatitude()) / deltaLatitude * this.getHeight()),
                (int) ((street.getDestination().getLongitude() - map.getMinLongitude()) / deltaLongitude * this.getWidth()),
                (int) ((deltaLatitude - street.getDestination().getLatitude() + map.getMinLatitude()) / deltaLatitude * this.getHeight())
        ));

    }
}
