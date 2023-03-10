package com.deliverif.app.vues;

import com.deliverif.app.models.map.CityMap;

import javax.swing.*;
import java.awt.*;

public class Map extends JPanel {

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        CityMap map = (CityMap) this.getClientProperty("map");

        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(3));
        g.drawLine(20, 20, 400, 400);
    }
}
