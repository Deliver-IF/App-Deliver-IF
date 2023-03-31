package com.deliverif.app.controller;

import com.deliverif.app.exceptions.NoConfiguredDeliveryException;
import com.deliverif.app.exceptions.NoCourierAvailableException;
import com.deliverif.app.exceptions.WrongDeliveryTimeException;
import com.deliverif.app.exceptions.WrongSelectedMapException;
import com.deliverif.app.model.*;
import com.deliverif.app.services.DeliveryService;
import com.deliverif.app.utils.Constants;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import lombok.Getter;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.Optional;

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
    private ComboBox<Courier> selectCourierComboBox;
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
    private ChoiceBox<Courier> courierChoiceBox;
    @FXML
    private ChoiceBox<String> timeWindowChoiceBox;
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
    @FXML
    private Text itineraryDetailArriveAt;
    @FXML
    private Text itineraryDetailDeliveryTime;
    @FXML
    private AnchorPane itineraryDetailAnchorPane;

    private Courier allCourierFilter;

    private final HashMap<String, Integer> timeWindowsHashMap = new HashMap<>();
    @FXML
    private Button submitDeliveryRequestButton;
    @FXML
    private Button deleteDeliveryRequestButton;
    @FXML
    private Button editDeliveryRequestButton;

    public MainController() {
        this.dataModel = new DataModel();
    }

    /**
     * Method called by the FXML loader after ressource file reading
     */
    @FXML
    public void initialize() {
        allCourierFilter = Courier.create(-1, "All");
        selectCourierComboBox.getItems().add(allCourierFilter);
        selectCourierComboBox.setValue(allCourierFilter);

        itineraryDetailAnchorPane.setPrefHeight(
                Toolkit.getDefaultToolkit().getScreenSize().getHeight()
        );
    }

    public void warningDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Display a dialog window to select a map on the user's device.
     * If the map is successfully loaded, it is displayed on the map pane.
     */
    @FXML
    public void loadMapAction() {
        Optional<ButtonType> alertResult = showWarningOnLoad();
            if (alertResult.isPresent() && alertResult.get() == ButtonType.CANCEL) {
                return;
            }
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(menuBar.getScene().getWindow());
            if (file != null) {
                loadFile(file);
            }
    }


    @FXML
    public void loadSmallMap() {
        Optional<ButtonType> alertResult = showWarningOnLoad();
        if (alertResult.isPresent() && alertResult.get() == ButtonType.CANCEL) {
            return;
        }
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/com/deliverif/app/maps/smallMap.xml");
        loadFile(file);
    }

    @FXML
    public void loadMediumMap() {
        Optional<ButtonType> alertResult = showWarningOnLoad();
        if (alertResult.isPresent() && alertResult.get() == ButtonType.CANCEL) {
            return;
        }
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/com/deliverif/app/maps/mediumMap.xml");
        loadFile(file);
    }

    @FXML
    public void loadLargeMap() {
            Optional<ButtonType> alertResult = showWarningOnLoad();
            if (alertResult.isPresent() && alertResult.get() == ButtonType.CANCEL) {
                return;
            }
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
                    this.nbCourierText.setText(Integer.toString(citymap.getDeliveryTours().size()));
                    this.selectCourierComboBox.getItems().clear();
                    this.selectCourierComboBox.getItems().add(allCourierFilter);
                    this.selectCourierComboBox.setValue(allCourierFilter);
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
                    MapController.eraseShapes(this.mapPane, deliveryTour.getShapes());
                }
                this.dataModel.loadTourFromFile(file);
                for (DeliveryTour deliveryTour: this.dataModel.getCityMap().getDeliveryTours().values()) {
                    MapController.displayDeliveryTour(this.mapPane, this.dataModel.getCityMap(), deliveryTour);
                }
                this.nbCourierText.setText(Integer.toString(this.dataModel.getCityMap().getDeliveryTours().size()));
                this.selectCourierComboBox.getItems().clear();
                this.selectCourierComboBox.getItems().add(allCourierFilter);
                for (DeliveryTour deliveryTour: this.dataModel.getCityMap().getDeliveryTours().values()) {
                    this.selectCourierComboBox.getItems().add(deliveryTour.getCourier());
                }
                this.selectCourierComboBox.setValue(allCourierFilter);
            } catch (FileNotFoundException | WrongSelectedMapException e) {
                warningDialog("Information", null, e.getMessage());
            } catch (Exception exc) {
                System.err.println("Error while loading tour file");
                System.err.println(exc.getMessage());
                warningDialog("Information", null, exc.getMessage());
            }
        }
    }

    /**
     * Display a dialog window to select a file on the user's device.
     * If the tour is successfully loaded, it is displayed on the map pane.
     */
    @FXML
    public void saveTourAction() {
        if (this.dataModel.getCityMap() == null) {
            warningDialog("Information", null, "No map loaded");
            return;
        }
        if (this.dataModel.getCityMap().getDeliveryTours().size() == 0) {
            warningDialog("Information", null, "No tour loaded");
            return;
        }
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(menuBar.getScene().getWindow());
        if (file != null) {
            try {
                this.dataModel.saveTourToFile(file);
            } catch (FileAlreadyExistsException | NoConfiguredDeliveryException e) {
                warningDialog("Information", null, e.getMessage());
            } catch (Exception exc) {
                System.err.println("Error while saving tour file");
                System.err.println(exc.getMessage());
                warningDialog("Information", null, exc.getMessage());
            }
        }
    }

    /**
     * Make visible the dialog box to enter the name of the new courier
     */
    @FXML
    public void createCourier() {
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
        if (citymap != null) {
            String courierName = this.courierNameTextField.getText();
            DeliveryTour newDeliveryTour =  citymap.addDeliveryTour((selectCourierComboBox.getValue()) == allCourierFilter);
            if (newDeliveryTour != null) {
                newDeliveryTour.getCourier().setCourierName(courierName);
                int nbCourier = citymap.getDeliveryTours().size();
                this.nbCourierText.setText(Integer.toString(nbCourier));
                selectCourierComboBox.getItems().add(newDeliveryTour.getCourier());

                if (nbCourier == 1) {
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

        if (citymap != null) {
            int nbCourier = citymap.getDeliveryTours().size();
            if (nbCourier >= 1){
                try {
                    citymap.deleteDeliveryTour();
                    nbCourier = citymap.getDeliveryTours().size();
                    this.nbCourierText.setText(Integer.toString(nbCourier));
                    if (nbCourier == 0) {
                        this.decreaseCourierButton.setDisable(true);
                    }

                    // We remove the name of the courier that has been removed
                    Courier currentCourierFilter = selectCourierComboBox.getValue();
                    selectCourierComboBox.getItems().clear();
                    selectCourierComboBox.getItems().add(allCourierFilter);
                    if (dataModel.getCityMap().getDeliveryTours().get(currentCourierFilter.getIdCourier()) != null) {
                        selectCourierComboBox.setValue(currentCourierFilter);
                    } else {
                        selectCourierComboBox.setValue(allCourierFilter);
                    }
                    for (DeliveryTour deliveryTour : citymap.getDeliveryTours().values()) {
                        if (dataModel.getCityMap().getDeliveryTours().get(currentCourierFilter.getIdCourier()) != null) {
                            selectCourierComboBox.getItems().add(deliveryTour.getCourier());
                        }
                    }
                } catch (NoCourierAvailableException e) {
                    warningDialog("Information", null, e.getMessage());
                }
            }
        }
    }

    @FXML
    protected void updateCourierViewFilter() {
        Courier courierViewFilter = selectCourierComboBox.getValue();
        if (courierViewFilter != null) {
            for (DeliveryTour deliveryTour : dataModel.getCityMap().getDeliveryTours().values()) {
                if (courierViewFilter.getCourierName().equals("All") || deliveryTour.getCourier().getCourierName().equals(courierViewFilter.getCourierName())) {
                    MapController.changeCourierPathVisibility(deliveryTour, true);
                } else {
                    MapController.changeCourierPathVisibility(deliveryTour, false);
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
            MapController.currentlySelectedIntersection.getDefaultShapesOnMap().get(0).setFill(Constants.BASE_MAP_INTERSECTION_COLOR);
        }
        MapController.currentlySelectedIntersection = null;
        MapController.currentlySelectedDeliveryRequest = null;
        editDeliveryRequestButton.setVisible(false);
        deleteDeliveryRequestButton.setVisible(false);
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
        if (MapController.currentIndex == 0) {
            prevDeliveryPointInfo.setVisible(true);
        }
        MapController.currentIndex++;
        deliveryWindow.setText(getDeliveryInfoDialogContent());
        MapController.currentlySelectedDeliveryRequest = MapController.currentDeliveryRequests.get(MapController.currentIndex);

        if (MapController.currentIndex == MapController.currentDeliveryRequests.size() - 1) {
            nextDeliveryPointInfo.setVisible(false);
        }
    }

    /**
     * If the user clicks on the "previous delivery request button" (<), the pop-up loads the information
     * of the previous delivery request on the schedule and displays it.
     */
    @FXML
    protected void prevDeliveryPointInfoDialog() {
        if (MapController.currentIndex == MapController.currentDeliveryRequests.size() - 1) {
            nextDeliveryPointInfo.setVisible(true);
        }
        MapController.currentIndex--;
        MapController.currentlySelectedDeliveryRequest = MapController.currentDeliveryRequests.get(MapController.currentIndex);
        deliveryWindow.setText(getDeliveryInfoDialogContent());

        if (MapController.currentIndex == 0) {
            prevDeliveryPointInfo.setVisible(false);
        }
    }

    /**
     * Display a pop-up when the user clicks on an intersection / delivery point.
     * If they are some delivery request at this intersection, they are displayed.
     * The user can also use this pop-up to create a new delivery request.
     */
    @FXML
    protected void addDeliveryRequestDialog() {
        if(this.dataModel.getCityMap().getDeliveryTours().size() != 0) {
            newDeliveryRequestDialogPane.setVisible(true);
            int start_hour = 8;
            int limit_hour = 12;
            timeWindowsHashMap.clear();

            courierChoiceBox.getItems().clear();
            timeWindowChoiceBox.getItems().clear();

            for (DeliveryTour deliveryTour : dataModel.getCityMap().getDeliveryTours().values()) {
                courierChoiceBox.getItems().add(deliveryTour.getCourier());
            }

            for (int hour = start_hour; hour + 1 <= limit_hour; hour++) {
                String timeWindowText = intToTimeWindowText(hour);
                timeWindowChoiceBox.getItems().add(timeWindowText);
                timeWindowsHashMap.put(timeWindowText, hour);
            }
        } else {
            warningDialog("Warning", "No courier", "You cannot enter a new delivery request because there are no couriers ");
        }
    }

    /**
     * Open the delivery request dialog pane with the ability to edit and delete it.
     */
    @FXML
    protected void editDeliveryRequestDialog() {
        addDeliveryRequestDialog();

        if (MapController.currentlySelectedDeliveryRequest != null) {
            // If we are editing a delivery request
            courierChoiceBox.setValue(MapController.currentlySelectedDeliveryRequest.getDeliveryTour().getCourier());
            timeWindowChoiceBox.setValue(intToTimeWindowText(MapController.currentlySelectedDeliveryRequest.getStartTimeWindow()));
            submitDeliveryRequestButton.setText("Save");
        }
    }

    /**
     * Given an int, creates a string representing the time window in the format "XX.00 am/pm - XX.00 am/pm"
     *
     * @param hour  the start hour of the time window.
     * @return      the formatted string representing the time window.
     */
    private String intToTimeWindowText(int hour) {
        String start_am_or_pm = hour <= 11 ? "am" : "pm";
        String end_am_or_pm = (hour + 1) <= 11 ? "am" : "pm";
        String timeWindowText = hour + ".00 " + start_am_or_pm + " - " + (hour + 1) + ".00 " + end_am_or_pm;

        return timeWindowText;
    }

    /**
     * Close the currently open "Add a new delivery request" pop-up.
     */
    @FXML
    protected void closeAddDeliveryRequestDialogPane() {
        mapPane.getScene().lookup("#noRouteFound").setVisible(false);
        newDeliveryRequestDialogPane.setVisible(false);
        submitDeliveryRequestButton.setText("Add");
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
    protected void submitDeliveryRequest() throws Exception {
        if (courierChoiceBox.getValue() != null && timeWindowChoiceBox.getValue() != null) {
            DeliveryTour deliveryTour = dataModel.getCityMap().getDeliveryTours().get((courierChoiceBox.getValue()).getIdCourier());
            DeliveryRequest deliveryRequest;

            if (deliveryTour == null) {
                warningDialog("Information", null, "The courier with id "+courierChoiceBox.getValue()+" doesn't exist anymore");
                return;
            }

            if (submitDeliveryRequestButton.getText().equals("Add")) {
                // We are creating a new delivery request;
                deliveryRequest = new DeliveryRequest(timeWindowsHashMap.get(timeWindowChoiceBox.getValue()), MapController.currentlySelectedIntersection, deliveryTour);

            } else if (submitDeliveryRequestButton.getText().equals("Save")) {
                // We are editing a delivery request
                deliveryRequest = MapController.currentlySelectedDeliveryRequest;
                deliveryRequest.getDeliveryTour().removeDeliveryRequest(deliveryRequest);
                DeliveryService.getInstance().searchOptimalDeliveryTour(deliveryRequest.getDeliveryTour());
                MapController.displayDeliveryTour(mapPane, dataModel.getCityMap(), deliveryRequest.getDeliveryTour());
                deliveryRequest.setDeliveryTour(deliveryTour);
            } else {
                throw new Exception("The content of the submit button does not match any possible value.");
            }

            deliveryTour.addDeliveryRequest(deliveryRequest);
            Text noRouteFoundText = (Text) mapPane.getScene().lookup("#noRouteFound");
            try {
                DeliveryService.getInstance().searchOptimalDeliveryTour(deliveryTour);

                // Draw the delivery tour on the map
                closeAddDeliveryRequestDialogPane();
                closeIntersectionInfoDialog();
                MapController.displayDeliveryTour(mapPane, dataModel.getCityMap(), deliveryTour);
            } catch (IllegalStateException | WrongDeliveryTimeException e) {
                deliveryTour.removeDeliveryRequest(deliveryRequest);
                noRouteFoundText.setVisible(true);
            }
        } else {
            warningDialog("Warning", null, "You need to fill all the fields to create a delivery request !");
        }
    }

    /**
     * Delete a delivery request.
     * It is removed from a delivery tour and erased on the map pane.
     *
     * @throws Exception
     */
    @FXML
    protected void deleteDeliveryRequest() throws Exception {
        DeliveryRequest deliveryRequest = MapController.currentlySelectedDeliveryRequest;
        DeliveryTour deliveryTour = deliveryRequest.getDeliveryTour();

        deliveryTour.removeDeliveryRequest(deliveryRequest);
        DeliveryService.getInstance().searchOptimalDeliveryTour(deliveryTour);
        MapController.currentlySelectedIntersection.getDefaultShapesOnMap().remove(deliveryRequest.getDeliveryRequestCircle());

        MapController.displayDeliveryTour(mapPane, dataModel.getCityMap(), deliveryTour);
        closeIntersectionInfoDialog();
    }

    public void onWindowSizeChangeEventHandler() {
        if (mapPane.getChildren().size() != 0) {
            mapPane.getChildren().clear();
            this.dataModel.getMapController().drawBasemap(this.mapPane, this.dataModel.getCityMap());
            for (DeliveryTour deliveryTour: this.dataModel.getCityMap().getDeliveryTours().values()) {
                MapController.displayDeliveryTour(this.mapPane, this.dataModel.getCityMap(), deliveryTour);
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

    protected Optional<ButtonType> showWarningOnLoad() {
        Optional<ButtonType> result = Optional.empty();
        if (this.dataModel.getCityMap() != null && !this.dataModel.getCityMap().getDeliveryTours().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "There is some changes that aren't saved, would you like to continue ?");
            alert.setHeaderText(null);
            alert.setTitle("Unsaved changes detected");
            result = alert.showAndWait();
        }
        return result;
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
        int durationDelivery;

        // If indexDestinationDeliveryRequest == 0, then it means the origin is the warehouse
        if(indexSelectedDeliveryRequest > 0) {
            DeliveryRequest deliveryRequestOrigin = selectedDeliveryTour.getStops().get(indexSelectedDeliveryRequest - 1);
            intersectionOrigin = deliveryRequestOrigin.getIntersection();
            durationDelivery = selectedDeliveryRequest.getArrivalTime() - deliveryRequestOrigin.getArrivalTime();
        } else {
            intersectionOrigin = dataModel.getCityMap().getWarehouse();
            durationDelivery = selectedDeliveryRequest.getArrivalTime() - 8 * 60;
        }

        String indicationText = "";
        String distanceText = "";

        itineraryDetailArriveAt.setText(
                "Arrive at "
                + String.format("%02d", selectedDeliveryRequest.getArrivalTime() / 60) + "h"
                + String.format("%02d", selectedDeliveryRequest.getArrivalTime() % 60)
        );
        itineraryDetailDeliveryTime.setText(
                durationDelivery + " min ..........."
        );

        String currentStreetName = "";
        float distanceStreet = 0;

        for (Segment streetToTake : selectedDeliveryTour.getTour()) {

            if(streetToTake.getOrigin().equals(intersectionOrigin) || (hasStart && !isDestinationreached)) {
                hasStart = true;

                if(streetToTake.getName().equals(currentStreetName)) {
                    distanceStreet += streetToTake.getLength();
                } else if (!currentStreetName.equals("")) {
                    indicationText = "Turn " + currentStreetName;
                    distanceText = new BigDecimal(distanceStreet).setScale(2, BigDecimal.ROUND_UP) + " m";

                    addTextBlock(indicationText, distanceText);

                    currentStreetName = streetToTake.getName();
                    distanceStreet = streetToTake.getLength();
                } else {
                    currentStreetName = streetToTake.getName();
                    distanceStreet = streetToTake.getLength();
                }

            }

            if (streetToTake.getDestination().equals(intersectionDestination)) {
                isDestinationreached = true;

                indicationText = "Turn " + currentStreetName;
                distanceText = new BigDecimal(distanceStreet).setScale(2, BigDecimal.ROUND_UP) + " m";

                addTextBlock(indicationText, distanceText);
                Text destinationreachedText = createText("Your destination is there");
                itineraryDetail.getChildren().add(destinationreachedText);
            }
        }

        itineraryDetailAnchorPane.setPrefHeight(itineraryDetailAnchorPane.getPrefHeight() + shiftY);

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
