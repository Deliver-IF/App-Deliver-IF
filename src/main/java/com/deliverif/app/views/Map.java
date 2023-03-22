package com.deliverif.app.views;

import com.deliverif.app.controllers.MainController;
import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.Intersection;
import com.deliverif.app.models.map.Segment;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class Map extends Region {
    int numberOfCouriers = 0;
    boolean mapDrawn = false;

    public Map() {}

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

    /**
     * Draw the streets of a map.
     *
     * @param mapPane
     * @param map
     */
    public void drawBasemap(Pane mapPane, CityMap map) {
        if (!mapDrawn) {
            displayStreets(mapPane, map, map.getStreets(), Color.GRAY);
            displayCrossings(mapPane, map, map.getIntersections(), Color.TRANSPARENT);
            displayWarehouse(mapPane, map, map.getWarehouse(), Color.BLACK);
            mapDrawn = true;
        }
    }

    private void displayStreets(Pane mapPane, CityMap map, Collection<Segment> streets, Color color) {
        for (Segment street : streets) {
            displaySegment(mapPane, map, street, color);
        }
    }

    private void displayCrossings(Pane mapPane, CityMap map, Collection<Intersection> intersections, Color color) {
        for(Intersection intersection : intersections) {
            displayIntersection(mapPane, map, intersection, color);
        }
    }

    /**
     * Draw the path followed of a courier.
     *
     * @param mapPane
     * @param map
     * @param streetsList
     */
    public void displayCourierPath(Pane mapPane, CityMap map, ArrayList<Segment> streetsList, Set<Intersection> deliveryPoints) {
        Color color = Color.rgb(joaat(numberOfCouriers+1) & 255, joaat(numberOfCouriers+1) >> 16 & 255, joaat(numberOfCouriers+1) >> 31 & 255);

        // Streets
        displayStreets(mapPane, map, streetsList, color);

        // Delivery Points
        displayCrossings(mapPane, map, deliveryPoints, color);

        numberOfCouriers++;
    }

    /**
     * Remove the path followed of a courier.
     */
    static public void hideCourierPath() {
        // TODO
    }

    static private void displayIntersection(Pane mapPane, CityMap map, Intersection intersection, Paint color) {
        Coordinates origin = getCoordinates(mapPane, map, intersection.getLongitude(), intersection.getLatitude());

        // Determine all properties of the shape we will draw on map
        Circle point = intersection.getDefaultShapeOnMap();
        point.setCenterX(origin.getX());
        point.setCenterY(origin.getY());
        point.setRadius(5);
        point.setStroke(color);
        point.setFill(color);
        point.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(intersection);
                System.out.println(intersection.getId());
                Text text = (Text) mapPane.getScene().lookup("#deliveryWindow");
                text.setText("Coucou minouuuuuu " + intersection.getId());
            }
        });
        point.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mapPane.getScene().setCursor(Cursor.HAND);
            }
        });
        point.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mapPane.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        intersection.setDefaultShapeOnMap(point);

        mapPane.getChildren().add(point);
    }

    static private void displaySegment(Pane mapPane, CityMap map, Segment segment, Paint color) {
        Coordinates origin = getCoordinates(mapPane, map, segment.getOrigin().getLongitude(), segment.getOrigin().getLatitude());
        Coordinates destination = getCoordinates(mapPane, map, segment.getDestination().getLongitude(), segment.getDestination().getLatitude());
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

    static private void displayWarehouse(Pane mapPane, CityMap map, Intersection warehouse, Paint color) {
        Coordinates origin = getCoordinates(mapPane, map, warehouse.getLongitude(), warehouse.getLatitude());

        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(20);
        rectangle.setHeight(20);
        rectangle.setX(origin.getX() - rectangle.getWidth()/2.);
        rectangle.setY(origin.getY() - rectangle.getHeight()/2.);

        rectangle.setStroke(color);
        rectangle.setFill(color);
        mapPane.getChildren().add(rectangle);
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

    static private int joaat(int value) {
        int hash = value;
        hash += (hash << 10);
        hash ^= (hash >> 6);

        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);

        return hash;
    }
}
