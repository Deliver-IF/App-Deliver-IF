package com.deliverif.app.views;

import com.deliverif.app.models.map.CityMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class Map extends Region {
    static public void draw(Canvas canvas, CityMap map) {
        GraphicsContext g = canvas.getGraphicsContext2D();

        float deltaLongitude = map.getMaxLongitude() - map.getMinLongitude();
        float deltaLatitude = map.getMaxLatitude() - map.getMinLatitude();

        g.setStroke(Color.GRAY);
        g.setLineWidth(3);

        map.getSegments().forEach(street -> g.strokeLine(
                (int) ((street.getOrigin().getLongitude() - map.getMinLongitude()) / deltaLongitude * canvas.getWidth()),
                (int) ((deltaLatitude - street.getOrigin().getLatitude() + map.getMinLatitude()) / deltaLatitude * canvas.getHeight()),
                (int) ((street.getDestination().getLongitude() - map.getMinLongitude()) / deltaLongitude * canvas.getWidth()),
                (int) ((deltaLatitude - street.getDestination().getLatitude() + map.getMinLatitude()) / deltaLatitude * canvas.getHeight())
        ));
    }
}
