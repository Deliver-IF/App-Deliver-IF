package com.deliverif.app.controller;

import com.deliverif.app.model.*;
import com.deliverif.app.services.DeliveryService;
import com.deliverif.app.utils.Constants;
import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Collection;

@Getter
public class MapController {
    public static ArrayList<DeliveryRequest> currentDeliveryRequests = new ArrayList<>();
    public static Intersection currentlySelectedIntersection;
    public static DeliveryRequest currentlySelectedDeliveryRequest;

    public boolean mapDrawn;

    public static int currentIndex = 0;
    public MapController() {}

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

    // ------------------------------------------- //
    // ------------- Base of the Map ------------- //
    // ------------------------------------------- //

    /**
     * Draw the whole map (streets, warehouse, etc.)
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     * @param map the object with all map elements
     */
    public void drawBasemap(Pane mapPane, CityMap map) {
            displayCrossings(mapPane, map, map.getIntersections().values());
            displayStreets(mapPane, map, map.getStreets(), Constants.BASE_MAP_STREETS_COLOR);
            displayWarehouse(mapPane, map, map.getWarehouse(), Constants.WAREHOUSE_COLOR);
            mapDrawn = true;
    }

    /**
     * Erase the whole map (streets, warehouse, etc.)
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     */
    public void eraseBasemap(Pane mapPane) {
        if (mapDrawn) {
            mapPane.getChildren().clear();
            mapDrawn = false;
        }
    }

    /**
     * Draw a collecion of intersections. Those intersections are the default one. They are invisible but allow
     * the user to interact with them.
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     * @param map the object with all map elements
     * @param intersections collectionintersection.setDefaultShapeOnMap(point); of streets we want to draw
     */
    private void displayCrossings(Pane mapPane, CityMap map, Collection<Intersection> intersections) {
        for (Intersection intersection : intersections) {
            displayIntersection(mapPane, map, intersection);
        }
    }

    /**
     * Draw a specific intersection. This intersection is a default one. It is invisible but allow
     *      * the user to interact with it.
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     * @param map the object with all map elements
     * @param intersection the intersection we are drawing
     */
    private void displayIntersection(
            Pane mapPane, CityMap map, Intersection intersection
    ) {
        Coordinates origin = getCoordinates(mapPane, map, intersection.getLongitude(), intersection.getLatitude());

        // Determine all properties of the shape we will draw on map
        Circle point = new Circle();
        point.setCenterX(origin.getX());
        point.setCenterY(origin.getY());
        point.setFill(Constants.BASE_MAP_INTERSECTION_COLOR);
        point.setRadius(Constants.INTERSECTION_SHAPE_RADIUS);

        // Click on an intersection
        point.setOnMouseClicked(mouseEvent -> {
            if (currentlySelectedIntersection != null && currentDeliveryRequests.size() == 0) {
                currentlySelectedIntersection
                        .getDefaultShapesOnMap()
                        .get(0)
                        .setFill(Constants.BASE_MAP_INTERSECTION_COLOR);
            }
            currentlySelectedIntersection = intersection;
            // Reset the DeliveryRequests for the current intersection. Indeed, if this part of the code is reach, it means
            // the intersection has no DeliveryRequest
            currentDeliveryRequests.clear();

            // Leave the selected intersection colored
            intersection.getDefaultShapesOnMap().get(0).setFill(Constants.BASE_MAP_INTERSECTION_COLOR_HOVER);

            // Change text on dialog
            Text deliveryWindowText = (Text) mapPane.getScene().lookup("#deliveryWindow");
            deliveryWindowText.setText("No delivery at this intersection");

            Button prevDeliveryPointInfo = (Button) mapPane.getScene().lookup("#prevDeliveryPointInfo");
            Button nextDeliveryPointInfo = (Button) mapPane.getScene().lookup("#nextDeliveryPointInfo");
            prevDeliveryPointInfo.setVisible(false);
            nextDeliveryPointInfo.setVisible(false);

            Text seeDetails = (Text) mapPane.getScene().lookup("#seeDetailsDeliveryRequestText");
            seeDetails.setVisible(false);

            properlyPlaceIntersectionInfoDialogPane(mapPane, origin);
        });

        // Event which change cursor on intersection hover
        point.setOnMouseEntered(mouseEvent -> {
            mapPane.getScene().setCursor(Cursor.HAND);
            intersection.getDefaultShapesOnMap().get(0).setFill(Constants.BASE_MAP_INTERSECTION_COLOR_HOVER);
        });
        point.setOnMouseExited(mouseEvent -> {
            mapPane.getScene().setCursor(Cursor.DEFAULT);
            if(currentlySelectedIntersection == null || currentlySelectedIntersection != intersection) {
                intersection.getDefaultShapesOnMap().get(0).setFill(Constants.BASE_MAP_INTERSECTION_COLOR);
            }
        });

        intersection.getDefaultShapesOnMap().clear();
        intersection.setDefaultShapeOnMap(point);

        mapPane.getChildren().add(point);
    }

    /**
     * Display the warehouse on the map. It is represented by a big square.
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     * @param map the object with all map elements
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

    // ------------------------------------------- //
    // -------------- Delivery Tour -------------- //
    // ------------------------------------------- //

    /**
     * Draw the path followed by a courier and all stops
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     * @param map the object with all map elements
     * @param deliveryTour the deliveryTour to draw
     */
    public static void displayDeliveryTour(Pane mapPane, CityMap map, DeliveryTour deliveryTour) {
        Color color = Color.rgb(
            joaat(deliveryTour.getCourier().getIdCourier()) & 255,
            joaat(deliveryTour.getCourier().getIdCourier()) >> 16 & 255,
            joaat(deliveryTour.getCourier().getIdCourier()) >> 31 & 255
        );

        // First, we erase the current route as editing a delivery tour often changes the original route.
        eraseShapes(mapPane, deliveryTour.getShapes());
        deliveryTour.getShapes().clear();

        // Streets
        for (Segment segment : deliveryTour.getTour()) {
            Line lineDrawn = displaySegment(mapPane, map, segment, color, deliveryTour.isVisible());
            deliveryTour.addShape(lineDrawn);
        }
        // Delivery Points
        for (DeliveryRequest deliveryRequest : deliveryTour.getStops()) {
            Circle deliveryPoint = displayDeliveryPoint(mapPane, map, deliveryRequest.getIntersection(), color, deliveryRequest);
            deliveryTour.addShape(deliveryPoint);
        }
    }


    /**
     * Hide the path followed by a courier.
     */
    static public void changeCourierPathVisibility(DeliveryTour deliveryTour, boolean visible) {
        for (Shape shape : deliveryTour.getShapes()) {
            shape.setVisible(visible);
        }
    }

    /**
     * Display a delivery point on the map pane.
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     * @param map the object with all map elements
     * @param intersection the Intersection representing the delivery point.
     * @param color         the color that has to be used to draw the delivery point.
     * @param deliveryRequest the delivery request we try to display, in order to retrieve all the information related to it
     */
    private static Circle displayDeliveryPoint(
            Pane mapPane, CityMap map, Intersection intersection, Paint color, DeliveryRequest deliveryRequest
    ) {
        Coordinates origin = getCoordinates(mapPane, map, intersection.getLongitude(), intersection.getLatitude());

        // Determine all properties of the shape we will draw on map
        Circle point = deliveryRequest.getDeliveryRequestCircle();
        point.setCenterX(origin.getX());
        point.setCenterY(origin.getY());
        point.setStroke(color);
        point.setFill(color);
        point.setRadius(Constants.DELIVERY_REQUEST_SHAPE_RADIUS);
        point.setVisible(deliveryRequest.getDeliveryTour().isVisible());

        point.setOnMouseClicked(mouseEvent -> {
            if (currentlySelectedIntersection != null) {
                currentlySelectedIntersection.getDefaultShapesOnMap().get(0).setFill(Constants.BASE_MAP_INTERSECTION_COLOR);
            }

            Text deliveryWindowText = (Text) mapPane.getScene().lookup("#deliveryWindow");
            if (currentlySelectedIntersection != null && currentDeliveryRequests.size() == 0) {
                currentlySelectedIntersection.getDefaultShapesOnMap().get(0).setFill(Constants.BASE_MAP_INTERSECTION_COLOR);
            }

            currentDeliveryRequests = DeliveryService.getInstance().getAllDeliveryRequestFromIntersection(map, intersection);
            currentlySelectedIntersection = intersection;
            currentlySelectedDeliveryRequest = deliveryRequest;
            currentIndex = currentDeliveryRequests.size() - 1;

            deliveryWindowText.setText(MainController.getDeliveryInfoDialogContent());

            Text seeDetails = (Text) mapPane.getScene().lookup("#seeDetailsDeliveryRequestText");
            seeDetails.setVisible(true);

            Button prevDeliveryPointInfo = (Button) mapPane.getScene().lookup("#prevDeliveryPointInfo");
            Button nextDeliveryPointInfo = (Button) mapPane.getScene().lookup("#nextDeliveryPointInfo");
            prevDeliveryPointInfo.setVisible(currentIndex != 0);
            nextDeliveryPointInfo.setVisible(false);

            mapPane.getScene().lookup("#editDeliveryRequestButton").setVisible(true);
            mapPane.getScene().lookup("#deleteDeliveryRequestButton").setVisible(true);

            properlyPlaceIntersectionInfoDialogPane(mapPane, origin);
        });

        // Event which change cursor on intersection hover
        point.setOnMouseEntered(mouseEvent -> mapPane.getScene().setCursor(Cursor.HAND));
        point.setOnMouseExited(mouseEvent -> mapPane.getScene().setCursor(Cursor.DEFAULT));

        if (!intersection.getDefaultShapesOnMap().contains(point)) {
            intersection.setDefaultShapeOnMap(point);
        }
        if (!mapPane.getChildren().contains(point)) {
            mapPane.getChildren().add(point);
        }

        return point;
    }

    /**
     * Places the intersection's info dialog pane so that it fits inside the map pane.
     *
     * @param mapPane   the map pane to display the dialog pane on.
     * @param origin    the coordinates of the intersection.
     */
    private static void properlyPlaceIntersectionInfoDialogPane(Pane mapPane, Coordinates origin) {
        DialogPane dialogPane = (DialogPane) mapPane.getScene().lookup("#intersectionInfoDialog");
        double dialogPaneX;
        double dialogPaneY;

        if (mapPane.getWidth() < origin.getX() + (dialogPane.getWidth() / 2)) {
            dialogPaneX = mapPane.getWidth() - dialogPane.getWidth();
        } else if (origin.getX() - (dialogPane.getWidth() / 2) < 0.0) {
            dialogPaneX = 0.0;
        } else {
            dialogPaneX = origin.getX() - (dialogPane.getWidth() / 2);
        }

        if (origin.y - dialogPane.getHeight() - 10 < 0) {
            dialogPaneY = origin.y + 25;
        } else {
            dialogPaneY = origin.getY() - dialogPane.getHeight() - 10;
        }

        movePane(mapPane, dialogPane, dialogPaneX, dialogPaneY);
    }

    // ------------------------------------------- //
    // ----- Base of the Map & Delivery Tour ----- //
    // ------------------------------------------- //

    /**
     * Draw a collection of segments representing the streets of a map.
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     * @param map the object with all map elements
     * @param segments collection of streets we want to draw
     * @param color which color we want to draw the streets with
     */
    private static void displayStreets(Pane mapPane, CityMap map, Collection<Segment> segments, Color color) {
        for (Segment segment : segments) {
            displaySegment(mapPane, map, segment, color, true);
        }
    }

    /**
     * Display a segment on the map pane.
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     * @param map the object with all map elements
     * @param segment    the Segment representing the street.
     * @param color     the color that has to be used to draw the street.
     * @return          the Line object that has been drawn on the map pane.
     */
    static private Line displaySegment(Pane mapPane, CityMap map, Segment segment, Paint color, boolean visible) {
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
        line.setVisible(visible);
        mapPane.getChildren().add(line);

        Circle originPoint = segment
                .getOrigin()
                .getDefaultShapesOnMap()
                .get(segment.getOrigin().getDefaultShapesOnMap().size()-1);
        Circle destinationPoint = segment
                .getDestination()
                .getDefaultShapesOnMap()
                .get(segment.getDestination().getDefaultShapesOnMap().size()-1);
        if (mapPane.getChildren().remove(originPoint)) {
            mapPane.getChildren().add(originPoint);
        }

        if (mapPane.getChildren().remove(destinationPoint)) {
            mapPane.getChildren().add(destinationPoint);
        }

        return line;
    }

    /**
     * Erase a specific set of shapes on the map.
     *
     * @param mapPane   the map pane where the segments are drawn on.
     * @param shapes     a collection of shapes to remove.
     */
    public static void eraseShapes(Pane mapPane, Collection<Shape> shapes) {
        for (Shape shape : shapes) {
            eraseShape(mapPane, shape);
        }
    }

    /**
     * Erase a specific shape on the map.
     *
     * @param mapPane   the map pane where the segments are drawn on.
     * @param shape     a collection of shapes to remove.
     */
    public static void eraseShape(Pane mapPane, Shape shape) {
        mapPane.getChildren().remove(shape);
    }

    // ------------------------------------------- //
    // ---------------- Utilities ---------------- //
    // ------------------------------------------- //

    /**
     * Move the Dialog Pane which display information about Delivery Requests above and about the selected intersection.
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     * @param paneToMove the javafx pane we want to move (Dialog Pane in that case)
     * @param x abscissa coordinates where we want to move the pane on
     * @param y ordinate coordinates where we want to move the pane on
     */
    static private void movePane(Pane mapPane, DialogPane paneToMove, double x, double y) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(1), paneToMove);
        tt.setToX(x - (mapPane.getWidth() / 2.0) + (paneToMove.getWidth() / 2.0));
        tt.setToY(y - (mapPane.getHeight() / 2.0) + (paneToMove.getHeight() / 2.0) - Constants.DEFAULT_SHIFT_DELIVERY_POINT_DIALOG);
        tt.setCycleCount(1);
        tt.setAutoReverse(false);
        tt.play();

        paneToMove.setVisible(true);
    }

    /**
     * Returns the coordinates of a point on the map, from its latitude and longitude. The coordinates are relative to
     * the window
     *
     * @param mapPane the javafx graphic element where the map and all streets and intersections are drawn
     * @param map the object with all map elements
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
