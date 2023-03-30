package com.deliverif.app.controller;

import com.deliverif.app.exceptions.NoCourierUnavailableException;
import com.deliverif.app.model.*;
import com.deliverif.app.exceptions.WrongDeliveryTimeException;
import com.deliverif.app.services.DeliveryService;
import com.deliverif.app.utils.Constants;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.nio.file.FileAlreadyExistsException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Timer;

@Getter
public class MainController {
    DataModel dataModel;

    private static double shiftY = 0;

    // Map pane
    @FXML
    private Pane mapPane;

    // Menu bar
    @FXML
    private MenuBar  menuBar;
    @FXML
    private MenuItem loadMapMenuItem;
    @FXML
    private MenuItem loadTourMenuItem;
    @FXML
    private MenuItem saveTourMenuItem;
    @FXML
    private Menu aboutMenu;
    @FXML
    private Button addDeliveryButton;
    @FXML
    private Menu helpMenu;
    @FXML
    private Text nbCourierText;
    @FXML
    private Button increaseCourierButton;
    @FXML
    private Button decreaseCourierButton;
    @FXML
    private ComboBox selectCourierButton;
    // Itinerary - Delivery details
    @FXML
    private Text deliveryDetailsText;
    @FXML Text itineraryText;
    @FXML
    private DialogPane newDeliveryRequestDialogPane;
    @FXML
    private DialogPane intersectionInfoDialog;
    @FXML
    private Text deliveryWindow;
    @FXML
    private Button prevDeliveryPointInfo;
    @FXML
    private Button nextDeliveryPointInfo;
    @FXML
    private ChoiceBox courierChoiceBox;
    @FXML
    private ChoiceBox timeWindowChoiceBox;
    @FXML
    private Button closeAddDeliveryRequestDialogPane;
    @FXML
    private Group itineraryDetail;
    @FXML
    private VBox itineraryDetailSideBar;
    @FXML
    private ScrollPane itineraryDetailScrollPane;

    @FXML
    private DialogPane newCourierDialogPane;
    @FXML
    private TextField courierNameTextField;
    @FXML
    private Button addCourierButton;
    private HashMap<String, Integer> timeWindows = new HashMap<String, Integer>();

    public MainController() {
        this.dataModel = new DataModel();
    }

    /**
     * Method called by the FXML loader after ressource file reading
     */
    public void initialize() {
    }

    /**
     * Display a dialog window to select a map on the user's device.
     * If the map is successfully loaded, it is displayed on the map pane.
     */
    @FXML
    public void loadMapAction() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(menuBar.getScene().getWindow());
        loadFile(file);
    }


    @FXML
    public void loadSmallMap() {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/com/deliverif/app/maps/smallMap.xml");
        loadFile(file);
    }

    @FXML
    public void loadMediumMap() throws URISyntaxException {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/com/deliverif/app/maps/mediumMap.xml");
        loadFile(file);
    }

    @FXML
    public void loadLargeMap() {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/com/deliverif/app/maps/largeMap.xml");
        loadFile(file);
    }

    private void loadFile(File file) {
        // If a map has already been loaded, all the data entered on it is deleted and the old map is erased.
        if(this.dataModel.getMapController().mapDrawn) {
            this.dataModel.getMapController().eraseBasemap(this.mapPane);
        }
        if (file != null) {
            try {
                this.dataModel.loadMapFromFile(file);
                CityMap citymap = this.dataModel.getCityMap();
                if(citymap != null) {
                    // Maybe to delete
                    // citymap.addDeliveryTour();
                    this.nbCourierText.setText(Integer.toString(citymap.getDeliveryTours().size()));
                    this.increaseCourierButton.setDisable(false);
                    this.decreaseCourierButton.setDisable(true);
                }
            } catch (Exception exc) {
                // handle exception...
            }
        }
        //TODO: controller in model...
        this.dataModel.getMapController().drawBasemap(this.mapPane, this.dataModel.getCityMap());
    }

    /**
     * Display a dialog window to select a tour on the user's device.
     * If the tour is successfully loaded, it is displayed on the map pane.
     */
    @FXML
    public void loadTourAction() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(menuBar.getScene().getWindow());
        if (file != null) {
            try {
                for (DeliveryTour deliveryTour: this.dataModel.getCityMap().getDeliveryTours().values()) {
                    this.dataModel.getMapController().eraseShapes(this.mapPane, deliveryTour.getShapes());
                }
                this.dataModel.loadTourFromFile(file);
                for (DeliveryTour deliveryTour: this.dataModel.getCityMap().getDeliveryTours().values()) {
                    this.dataModel.getMapController().displayDeliveryTour(this.mapPane, this.dataModel.getCityMap(), deliveryTour);
                }
                this.nbCourierText.setText(Integer.toString(this.dataModel.getCityMap().getDeliveryTours().size()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            } catch (Exception exc) {
                System.err.println("Error while loading tour file");
                System.err.println(exc.getMessage());

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText(exc.getMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Display a dialog window to select a file on the user's device.
     * If the tour is successfully loaded, it is displayed on the map pane.
     */
    @FXML
    public void saveTourAction() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(menuBar.getScene().getWindow());
        if (file != null) {
            try {
                this.dataModel.saveTourToFile(file);
            } catch (FileAlreadyExistsException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            } catch (Exception exc) {
                System.err.println("Error while saving tour file");
                System.err.println(exc.getMessage());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText(exc.getMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Make visible the dialog box to enter the name of the new courier
     */
    @FXML
    public void createCourier(){
        this.courierNameTextField.clear();
        this.newCourierDialogPane.setVisible(true);
    }

    /**
     * Adds 1 to the number of couriers displayed
     * If there are 1 courier, we reactivate the decrement button
     */
    @FXML
    public void addCourier() {
        this.newCourierDialogPane.setVisible(false);
        CityMap citymap = this.dataModel.getCityMap();
        if(citymap != null) {
            String courierName = this.courierNameTextField.getText();
            DeliveryTour newDeliveryTour =  citymap.addDeliveryTour();
            if(newDeliveryTour != null) {
                newDeliveryTour.getCourier().setCourierName(courierName);
                int nbCourier = citymap.getDeliveryTours().size();
                this.nbCourierText.setText(Integer.toString(nbCourier));

                if(nbCourier == 1) {
                    this.decreaseCourierButton.setDisable(false);
                }
            }
        }
    }

    /**
     * Deduct 1 from the number of couriers displayed
     * If all couriers have at least one delivery assigned, an alert window opens to indicate that no courier can be deleted.
     * If there are 0 courier, the decrement button is disabled
     */
    @FXML
    public void deleteCourier() {
        CityMap citymap = this.dataModel.getCityMap();
        if(citymap != null) {
            int nbCourier = citymap.getDeliveryTours().size();
            if(nbCourier >= 1){
                try {
                    citymap.deleteDeliveryTour();
                    nbCourier = citymap.getDeliveryTours().size();
                    this.nbCourierText.setText(Integer.toString(nbCourier));
                    if(nbCourier == 0) {
                        this.decreaseCourierButton.setDisable(true);
                    }
                } catch (NoCourierUnavailableException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        }

    }

    /**
     * Close the currently open "Intersection info" pop-up.
     */
    @FXML
    protected void closeIntersectionInfoDialog() {
        intersectionInfoDialog.setVisible(false);
        // The circle is transparent again only if the selected Intersection doesn't have any DeliveryRequest
        if(MapController.currentDeliveryRequests.size() == 0) {
            MapController.currentlySelectedIntersection.getDefaultShapeOnMap().setFill(Constants.BASE_MAP_INTERSECTION_COLOR);
        }
        MapController.currentlySelectedIntersection = null;
    }

    /**
     * Return the content of the "Intersection info" pop-up.
     *
     * @return String
     */
    public static String getDeliveryInfoDialogContent() {
        int minutes = MapController.currentDeliveryRequests.get(MapController.currentIndex).getArrivalTime()%60;
        int hours = MapController.currentDeliveryRequests.get(MapController.currentIndex).getArrivalTime()/60;
        int startTimeWindow = MapController.currentDeliveryRequests.get(MapController.currentIndex).getStartTimeWindow();
        return "Delivery Window : " + startTimeWindow + "h-" + (startTimeWindow + 1) + "h\n" +
                "Arrival time : " + (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + "\n";
    }

    /**
     * If the user clicks on the "next delivery request button" (<), the pop-up loads the information
     * of the next delivery request on the schedule and displays it.
     */
    @FXML
    protected void nextDeliveryPointInfoDialog() {
        if(MapController.currentIndex == 0) {
            prevDeliveryPointInfo.setVisible(true);
        }
        MapController.currentIndex++;
        deliveryWindow.setText(getDeliveryInfoDialogContent());

        if(MapController.currentIndex == MapController.currentDeliveryRequests.size() - 1) {
            nextDeliveryPointInfo.setVisible(false);
        }

    }

    /**
     * If the user clicks on the "previous delivery request button" (<), the pop-up loads the information
     * of the previous delivery request on the schedule and displays it.
     */
    @FXML
    protected void prevDeliveryPointInfoDialog() {
        if(MapController.currentIndex == MapController.currentDeliveryRequests.size() - 1) {
            nextDeliveryPointInfo.setVisible(true);
        }
        MapController.currentIndex--;
        deliveryWindow.setText(getDeliveryInfoDialogContent());

        if(MapController.currentIndex == 0) {
            prevDeliveryPointInfo.setVisible(false);
        }

    }

    /**
     * Display a pop-up when the user clicks on an intersection / delivery point.
     * If they are some delivery request at this intersection, they are displayed.
     * The user can also use this pop-up to create a new delivery request.
     */
    @FXML
    protected void addDeliveryPointDialog() {
        if(this.dataModel.getCityMap().getDeliveryTours().size() != 0) {
            newDeliveryRequestDialogPane.setVisible(true);
            int start_hour = 8;
            int limit_hour = 12;
            timeWindows.clear();

            courierChoiceBox.getItems().clear();
            timeWindowChoiceBox.getItems().clear();

            for (DeliveryTour deliveryTour : dataModel.getCityMap().getDeliveryTours().values()) {
                courierChoiceBox.getItems().add(deliveryTour.getCourier());
            }

            for (int hour = start_hour; hour + 1 <= limit_hour; hour++) {
                String start_am_or_pm = hour <= 11 ? "am" : "pm";
                String end_am_or_pm = (hour + 1) <= 11 ? "am" : "pm";
                String timeWindowText = hour + ".00 " + start_am_or_pm + " - " + (hour + 1) + ".00 " + end_am_or_pm;
                timeWindowChoiceBox.getItems().add(timeWindowText);
                timeWindows.put(timeWindowText, hour);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No courier");
            alert.setContentText("You cannot enter a new delivery request because there are no couriers ");
            alert.showAndWait();
        }

    }

    /**
     * Close the currently open "Add a new delivery request" pop-up.
     */
    @FXML
    protected void closeAddDeliveryRequestDialogPane() {
        mapPane.getScene().lookup("#noRouteFound").setVisible(false);
        newDeliveryRequestDialogPane.setVisible(false);
    }

    /**
     * Close the currently open "New courier" pop-up.
     */
    @FXML
    protected void closeNewCourierDialogPane() {
        newCourierDialogPane.setVisible(false);
    }

    /**
     * Get the data from the currently open "Add new delivery request pop-up" and add the delivery request
     * to the selected courier.
     */
    @FXML
    protected void addDeliveryRequest() {
        DeliveryService deliveryService = DeliveryService.getInstance();
        DeliveryTour deliveryTour = dataModel.getCityMap().getDeliveryTours().get(((Courier) courierChoiceBox.getValue()).getIdCourier());
        if (deliveryTour == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("The courier with id "+courierChoiceBox.getValue()+" doesn't exist anymore");
            alert.showAndWait();
            return;
        }
        DeliveryRequest deliveryRequest = new DeliveryRequest(timeWindows.get((String) timeWindowChoiceBox.getValue()), MapController.currentlySelectedIntersection);
        deliveryTour.addDeliveryRequest(deliveryRequest);
        Text noRouteFoundText = (Text) mapPane.getScene().lookup("#noRouteFound");
        try {
            deliveryService.searchOptimalDeliveryTour(deliveryTour);

            // Draw the delivery tour on the map
            closeAddDeliveryRequestDialogPane();
            closeIntersectionInfoDialog();
            MapController MapController = new MapController();
            MapController.displayDeliveryTour(mapPane, dataModel.getCityMap(), deliveryTour);
        } catch (IllegalStateException | WrongDeliveryTimeException e) {
            deliveryTour.removeDeliveryRequest(deliveryRequest);
            noRouteFoundText.setVisible(true);
        }

    }

    public void onWindowSizeChangeEventHandler() {
        if (mapPane.getChildren().size() != 0) {
            mapPane.getChildren().clear();
            this.dataModel.getMapController().drawBasemap(this.mapPane, this.dataModel.getCityMap());
            for (DeliveryTour deliveryTour: this.dataModel.getCityMap().getDeliveryTours().values()) {
                this.dataModel.getMapController().displayDeliveryTour(this.mapPane, this.dataModel.getCityMap(), deliveryTour);
            }
        }
    }

    @FXML
    protected void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("About");
        ImageView imageInsa = new ImageView("https://logos.bde-insa-lyon.fr/insa/insa.png");
        imageInsa.setFitHeight(138);
        imageInsa.setPreserveRatio(true);
        alert.setGraphic(imageInsa);

        Label label = new Label("DELIVR'IF");
        label.setPadding(new Insets(15));

        Label copyright = new Label("Copyright © 2023");
        copyright.setPadding(new Insets(15));

        Text description = new Text("This application allows to manage a delivery system on a defined geographical area. " +
                "Delivery requests can be entered as well as the addition or removal of couriers. " +
                "The routes taken by the different couriers are visible on the map of the application.");
        description.setWrappingWidth(500);

        VBox content = new VBox(label,description,copyright);
        content.setAlignment(Pos.CENTER);
        alert.getDialogPane().setContent(content);
        alert.showAndWait();
    }

    @FXML
    protected void seeDetailsDeliveryRequest() {
        itineraryDetailScrollPane.setVisible(true);
        itineraryDetail.getChildren().clear();
        shiftY = 0;

        DeliveryRequest selectedDeliveryRequest = MapController.currentDeliveryRequests.get(MapController.currentIndex);
        DeliveryTour selectedDeliveryTour = DeliveryService.getInstance().getDeliveryTourFromDeliveryRequest(dataModel.getCityMap(), selectedDeliveryRequest);
        int indexSelectedDeliveryRequest = selectedDeliveryTour.getStops().indexOf(selectedDeliveryRequest);
        Intersection intersectionOrigin;
        Intersection intersectionDestination = selectedDeliveryRequest.getIntersection();

        boolean hasStart = false;
        boolean isDestinationreached = false;

        // If indexDestinationDeliveryRequest == 0, then it means the origin is the warehouse
        if(indexSelectedDeliveryRequest > 0) {
            System.out.println("Other");
            intersectionOrigin = selectedDeliveryTour.getStops().get(indexSelectedDeliveryRequest - 1).getIntersection();
        } else {
            System.out.println("Warehouse");
            intersectionOrigin = dataModel.getCityMap().getWarehouse();
        }

        String indication = "";
        String distance = "";
        System.out.println(MapController.currentlySelectedIntersection);
        System.out.println(intersectionOrigin.getId());
        for (Segment streetToTake : selectedDeliveryTour.getTour()) {

            System.out.println("origin = " + streetToTake.getOrigin().getId() + " / destination = " + streetToTake.getDestination().getId());

            if(streetToTake.getOrigin().equals(intersectionOrigin) && !isDestinationreached) {

                System.out.println(streetToTake);
                System.out.println(streetToTake.getName());

                hasStart = true;

                indication = "Turn " + streetToTake.getName();
                distance = new BigDecimal(streetToTake.getLength()).setScale(2, BigDecimal.ROUND_UP) + " m";

                addTextBlock(indication, distance);
            }

            if (streetToTake.getDestination().equals(intersectionDestination)) {
                isDestinationreached = true;
                Text destinationreachedText = createText("Your destination is there");
                itineraryDetail.getChildren().add(destinationreachedText);
            }
        }


    }

    private void addTextBlock(String indication, String distance) {
        // Indication of the direction
        Text indicationText = createText(indication);
        itineraryDetail.getChildren().add(indicationText);
        shiftY += 20;

        // Indication of the distance
        Text distanceText = createText(distance);
        itineraryDetail.getChildren().add(distanceText);
        shiftY += 30;
    }

    private Text createText(String text) {
        Text fxText = new Text();
        fxText.setText(text);
        fxText.setVisible(true);
        fxText.maxWidth(178);
        fxText.setLayoutY(shiftY);
        return fxText;
    }

    @FXML
    protected void closeItineraryDetailSideBar() {
        itineraryDetailScrollPane.setVisible(false);
    }
}
