package com.deliverif.app.views;

import com.deliverif.app.models.map.CityMap;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Map extends Region {
    static public void draw(Pane mapPane, CityMap map) {

        float deltaLongitude = map.getMaxLongitude() - map.getMinLongitude();
        float deltaLatitude = map.getMaxLatitude() - map.getMinLatitude();


        map.getSegments().forEach(street -> {
            Line l = new Line(
                    ((street.getOrigin().getLongitude() - map.getMinLongitude()) / deltaLongitude * mapPane.getWidth()),
                    ((deltaLatitude - street.getOrigin().getLatitude() + map.getMinLatitude()) / deltaLatitude * mapPane.getHeight()),
                    ((street.getDestination().getLongitude() - map.getMinLongitude()) / deltaLongitude * mapPane.getWidth()),
                    ((deltaLatitude - street.getDestination().getLatitude() + map.getMinLatitude()) / deltaLatitude * mapPane.getHeight())
            );
            l.setFill(Color.GRAY);
            l.setStrokeWidth(3);
            mapPane.getChildren().add(l);
        });
    }
}
