package com.deliverif.app.views;

import com.deliverif.app.models.map.CityMap;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import lombok.Getter;
import lombok.Setter;

public class Map extends Region {
    @Getter
    @Setter
    private static class Coordinates{
        private double x;
        private double y;

        public Coordinates(double x, double y) {
            this.x = x;
            this.y = y;
        }

    }
    static public void draw(Pane mapPane, CityMap map) {
        map.getSegments().forEach(street -> {
            Coordinates origin = getCoordinates(mapPane, map, street.getOrigin().getLongitude(), street.getOrigin().getLatitude());
            Coordinates destination = getCoordinates(mapPane, map, street.getDestination().getLongitude(), street.getDestination().getLatitude());
            Line l = new Line(
                    origin.getX(),
                    origin.getY(),
                    destination.getX(),
                    destination.getY()
            );
            l.setFill(Color.GRAY);
            l.setStrokeWidth(3);
            mapPane.getChildren().add(l);
        });
    }

    static private Coordinates getCoordinates(Pane mapPane, CityMap map, float longitude, float latitude) {
        float xRatio = map.getLongitudeRange() / (float) mapPane.getWidth();
        float yRatio = map.getLatitudeRange() / (float) mapPane.getHeight();
        // Get the best ratio to fit the map in the pane
        float ratio = Math.max(xRatio, yRatio);

        // For each coordinate we get the corresponding pixel position and we center it
        return new Coordinates(
            (longitude - map.getMinLongitude()) / ratio + (mapPane.getWidth()-map.getLongitudeRange()/ratio) / 2,
            (map.getLatitudeRange() - latitude + map.getMinLatitude()) / ratio + (mapPane.getHeight()-map.getLatitudeRange()/ratio) / 2
        );
    }
}
