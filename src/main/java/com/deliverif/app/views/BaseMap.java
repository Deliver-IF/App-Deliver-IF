package com.deliverif.app.views;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.Intersection;
import com.deliverif.app.models.map.Segment;
import com.deliverif.app.models.map.*;
import com.deliverif.app.services.DeliveryService;
import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

public class BaseMap extends Region {

    final Color BASE_MAP_STREETS_COLOR = Color.GRAY;
    final Color BASE_MAP_INTERSECTION_COLOR = Color.BLUE;
    final Color WAREHOUSE_COLOR = Color.BLACK;
    final int INTERSECTION_SHAPE_RADIUS = 2;
    final int DELIVERY_REQUEST_SHAPE_RADIUS = 5;

    int numberOfCouriers = 0;
    boolean mapDrawn = false;

    public static ArrayList<DeliveryRequest> currentDeliveryRequests = new ArrayList<>();

    public static int currentIndex = 0;

    public BaseMap() {}

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
     * Draw the all the streets of the map
     *
     * @param mapPane
     * @param map
     */
    public void drawBasemap(Pane mapPane, CityMap map) {
        if (!mapDrawn) {
            displayCrossings(mapPane, map, map.getIntersections(), BASE_MAP_INTERSECTION_COLOR);
            displayStreets(mapPane, map, map.getStreets(), BASE_MAP_STREETS_COLOR);
            displayWarehouse(mapPane, map, map.getWarehouse(), WAREHOUSE_COLOR);
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
     */
    public void displayDeliveryTour(Pane mapPane, CityMap map, DeliveryTour deliveryTour) {
        Color color = Color.rgb(joaat(numberOfCouriers+1) & 255, joaat(numberOfCouriers+1) >> 16 & 255, joaat(numberOfCouriers+1) >> 31 & 255);

        // Streets
        displayStreets(mapPane, map, deliveryTour.getTour(), color);
        // Delivery Points
        for (DeliveryRequest deliveryRequest : deliveryTour.getStops()) {
            displayDeliveryPoint(mapPane, map, deliveryRequest.getIntersection(), color, deliveryRequest);
        }
        System.out.println("Test 3");

        numberOfCouriers++;
    }

    /**
     * Remove the path followed by a courier.
     */
    static public void hideCourierPath() {
        // TODO
    }

    private void displayIntersection(
            Pane mapPane, CityMap map, Intersection intersection, Paint color
    ) {
        Coordinates origin = getCoordinates(mapPane, map, intersection.getLongitude(), intersection.getLatitude());

        // Determine all properties of the shape we will draw on map
        Circle point = new Circle();
        point.setCenterX(origin.getX());
        point.setCenterY(origin.getY());
        point.setStroke(color);
        point.setFill(color);
        point.setRadius(INTERSECTION_SHAPE_RADIUS);

        // Click on an intersection
        point.setOnMouseClicked(mouseEvent -> {
            // Change text on dialog
            Text deliveryWindowText = (Text) mapPane.getScene().lookup("#deliveryWindow");
            deliveryWindowText.setText("No delivery at this intersection");

            Button prevDeliveryPointInfo = (Button) mapPane.getScene().lookup("#prevDeliveryPointInfo");
            Button nextDeliveryPointInfo = (Button) mapPane.getScene().lookup("#nextDeliveryPointInfo");
            prevDeliveryPointInfo.setVisible(false);
            nextDeliveryPointInfo.setVisible(false);
            // Move dialog pane
            DialogPane dialogPane = (DialogPane) mapPane.getScene().lookup("#intersectionInfoDialog");
            movePane(
                    mapPane,
                    dialogPane,
                    origin.getX() - (dialogPane.getWidth() / 2),
                    origin.getY() - dialogPane.getHeight() - 20
            );
        });

        // Event which change cursor on intersection hover
        point.setOnMouseEntered(mouseEvent -> mapPane.getScene().setCursor(Cursor.HAND));
        point.setOnMouseExited(mouseEvent -> mapPane.getScene().setCursor(Cursor.DEFAULT));

        intersection.setDefaultShapeOnMap(point);

        mapPane.getChildren().add(point);
    }

    /**
     * Display a delivery point on the map pane.
     *
     * @param mapPane       the map pane to display the delivery point on.
     * @param map           the map containing the data (coordinates) of the different objects displayed on the map pane.
     * @param intersection the Intersection representing the delivery point.
     * @param color         the color that has to be used to draw the delivery point.
     * @param deliveryRequest the delivery request we try to display, in order to retrieve all the information related to it
     */
    private void displayDeliveryPoint(
            Pane mapPane, CityMap map, Intersection intersection, Paint color, DeliveryRequest deliveryRequest
    ) {
        Coordinates origin = getCoordinates(mapPane, map, intersection.getLongitude(), intersection.getLatitude());

        // Determine all properties of the shape we will draw on map
        Circle point = new Circle();
        point.setCenterX(origin.getX());
        point.setCenterY(origin.getY());
        point.setStroke(color);
        point.setFill(color);
        point.setRadius(DELIVERY_REQUEST_SHAPE_RADIUS);

        point.setOnMouseClicked(mouseEvent -> {
            System.out.println(origin.getX() + " " + origin.getY());

            Text deliveryWindowText = (Text) mapPane.getScene().lookup("#deliveryWindow");

            currentDeliveryRequests = DeliveryService.getInstance().getAllDeliveryRequestFromIntersection(map, intersection);
            currentIndex = currentDeliveryRequests.size() - 1;
//                logDeliveryRequest(deliveryRequest);

            deliveryWindowText.setText("Delivery Window : " + deliveryRequest.getStartTimeWindow() + "h-"
                            + (deliveryRequest.getStartTimeWindow() + 1) + "h\n"
            );

            Button prevDeliveryPointInfo = (Button) mapPane.getScene().lookup("#prevDeliveryPointInfo");
            Button nextDeliveryPointInfo = (Button) mapPane.getScene().lookup("#nextDeliveryPointInfo");
            if(currentIndex == 0) {
                prevDeliveryPointInfo.setVisible(false);
            } else {
                prevDeliveryPointInfo.setVisible(true);
            }
            nextDeliveryPointInfo.setVisible(false);

            DialogPane dialogPane = (DialogPane) mapPane.getScene().lookup("#intersectionInfoDialog");
            movePane(
                    mapPane,
                    dialogPane,
                    origin.getX() - (dialogPane.getWidth() / 2),
                    origin.getY() - dialogPane.getHeight() - 20
            );

            System.out.println("--- Logs ---");
            System.out.println(currentIndex);
            System.out.println(currentDeliveryRequests);

        });

        // Event which change cursor on intersection hover
        point.setOnMouseEntered(mouseEvent -> mapPane.getScene().setCursor(Cursor.HAND));
        point.setOnMouseExited(mouseEvent -> mapPane.getScene().setCursor(Cursor.DEFAULT));

        intersection.setDefaultShapeOnMap(point);

        System.out.println(point);
        mapPane.getChildren().add(point);
    }

    static private void movePane(Pane mapPane, DialogPane paneToMove, double x, double y) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(1), paneToMove);
        tt.setToX(x - (mapPane.getWidth() / 2.0) + (paneToMove.getWidth() / 2.0));
        tt.setToY(y - (mapPane.getHeight() / 2.0) + (paneToMove.getHeight() / 2.0) - 10.0);
        tt.setCycleCount(1);
        tt.setAutoReverse(false);
        tt.play();

        paneToMove.setVisible(true);
    }

    static private void logDeliveryRequest(DeliveryRequest deliveryRequest) {
        System.out.println("--- Intersection ---");
        System.out.println("Id = " + deliveryRequest.getIntersection().getId());
        System.out.println("Long = " + deliveryRequest.getIntersection().getLongitude());
        System.out.println("Lat = " + deliveryRequest.getIntersection().getLatitude());
        System.out.println("Time Window = " + deliveryRequest.getStartTimeWindow() + "h-"
                + (deliveryRequest.getStartTimeWindow() + 1)
                + "h\n");
    }

    /**
     * Display a street on the map pane.
     *
     * @param mapPane   the map pane to draw the street on.
     * @param map       the map containing the data (coordinates) of the different objects displayed on the map pane.
     * @param segment    the Segment representing the street.
     * @param color     the color that has to be used to draw the street.
     */
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

        Circle originSegment = segment.getOrigin().getDefaultShapeOnMap();
        Circle destinationSegment = segment.getDestination().getDefaultShapeOnMap();
        mapPane.getChildren().remove(originSegment);
        mapPane.getChildren().add(originSegment);
        mapPane.getChildren().remove(destinationSegment);
        mapPane.getChildren().add(destinationSegment);
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
