package com.deliverif.app.views;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.Intersection;
import com.deliverif.app.models.map.Segment;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

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
            for (Segment street : map.getSegments()) {
                displayStreet(mapPane, map, street, Color.GRAY);
            }
            displayWarehouse(mapPane, map, map.getWarehouse(), Color.BLACK);

            mapDrawn = true;
        }
    }

    /**
     * Draw the path followed of a courier.
     *
     * @param mapPane
     * @param map
     * @param streetsList
     */
    public void displayCourierPath(Pane mapPane, CityMap map, ArrayList<Segment> streetsList, ArrayList<Intersection> deliveryPoints) {
        Color color = Color.rgb(joaat(numberOfCouriers+1) & 255, joaat(numberOfCouriers+1) >> 16 & 255, joaat(numberOfCouriers+1) >> 31 & 255);

        // Streets
        for (Segment street : streetsList) {
            displayStreet(mapPane, map, street, color);
        }

        // Delivery Points
        for (Intersection deliveryPoint : deliveryPoints) {
            displayDeliveryPoint(mapPane, map, deliveryPoint, color);
        }

        numberOfCouriers++;
    }

    /**
     * Remove the path followed by a courier.
     */
    static public void hideCourierPath() {
        // TODO
    }

    /**
     * Display a delivery point on the map pane.
     *
     * @param mapPane       the map pane to display the delivery point on.
     * @param map           the map containing the data (coordinates) of the different objects displayed on the map pane.
     * @param deliveryPoint the Intersection representing the delivery point.
     * @param color         the color that has to be used to draw the delivery point.
     */
    static private void displayDeliveryPoint(Pane mapPane, CityMap map, Intersection deliveryPoint, Paint color) {
        Coordinates origin = getCoordinates(mapPane, map, deliveryPoint.getLongitude(), deliveryPoint.getLatitude());
        Circle point = new Circle(
                origin.getX(),
                origin.getY(),
                5
        );
        point.setStroke(color);
        point.setFill(color);
        mapPane.getChildren().add(point);
    }

    /**
     * Display a street on the map pane.
     *
     * @param mapPane   the map pane to draw the street on.
     * @param map       the map containing the data (coordinates) of the different objects displayed on the map pane.
     * @param street    the Segment representing the street.
     * @param color     the color that has to be used to draw the street.
     */
    static private void displayStreet(Pane mapPane, CityMap map, Segment street, Paint color) {
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

    /**
     * Display the warehouse on the map. It is represented by a big square.
     *
     * @param mapPane   the map pane to draw the warehouse on.
     * @param map       the map containing the data of the map.
     * @param warehouse the intersection representing the warehouse.
     * @param color     the color that has to be used to draw the warehouse.
     */
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

    /**
     * Returns the coordinates of a point on the map, from its latitude and longitude.
     *
     * @param mapPane   the map pane of a controller.
     * @param map       the map where the point will be drawn on.
     * @param longitude the longitude of the point to convert
     * @param latitude  the latitude of the point to convert
     * @return
     */
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

    /**
     *  Jenkins One-At-A-Time hash function.
     *
     * @param value the value to be hashed.
     * @return      the hashed value
     */
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
