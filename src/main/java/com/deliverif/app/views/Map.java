package com.deliverif.app.views;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.Intersection;
import com.deliverif.app.models.map.Segment;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

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

        // ---- Examples ----- //
        Segment segJeanRenoir = new Segment("Rue Jean Renoir",
                (float) 179.80255,
                new Intersection("25336175", (float) 4.863642, (float) 45.75301),
                new Intersection("25336178", (float) 4.8658633, (float) 45.752552)
        );
        Segment segClaudeRampon = new Segment("Imp. Claude Rampon",
                (float) 70.188835,
                new Intersection("522100855", (float) 4.866085, (float) 45.751476),
                new Intersection("1345284783", (float) 4.8660784, (float) 45.752106)
        );

        ArrayList<Segment> segmentCollection = new ArrayList<Segment>();
        segmentCollection.add(segJeanRenoir);
        segmentCollection.add(segClaudeRampon);
        // ------------------- //

        // Base map
        for (Segment street : map.getSegments()) {
            drawLine(mapPane, map, street, Color.GRAY);
        }

        // Couriers path
        for (Segment street : segmentCollection) {
            drawLine(mapPane, map, street, Color.RED);
        }
    }

    static private void drawLine(Pane mapPane, CityMap map, Segment street, Paint color) {
        Coordinates origin = getCoordinates(mapPane, map, street.getOrigin().getLongitude(), street.getOrigin().getLatitude());
        Coordinates destination = getCoordinates(mapPane, map, street.getDestination().getLongitude(), street.getDestination().getLatitude());
        Line line = new Line(
                origin.getX(),
                origin.getY(),
                destination.getX(),
                destination.getY()
        );
        line.setStroke(color);
        line.setStrokeWidth(3);
        mapPane.getChildren().add(line);
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
