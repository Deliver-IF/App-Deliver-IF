package com.deliverif.app.controller;

import com.deliverif.app.exceptions.NoCourierUnavailableException;
import com.deliverif.app.model.CityMap;
import com.deliverif.app.model.DataModel;
import com.deliverif.app.model.DeliveryRequest;
import com.deliverif.app.model.DeliveryTour;
import com.deliverif.app.services.DeliveryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import lombok.Getter;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.net.URISyntaxException;
import java.util.HashMap;

@Getter
public class MainController {
    DataModel dataModel;

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
        if (file != null) {
            try {
                this.dataModel.loadMapFromFile(file);
                CityMap citymap = this.dataModel.getCityMap();
                if(citymap != null) {
                    citymap.addDeliveryTour();
                    this.nbCourierText.setText(Integer.toString(citymap.getDeliveryTours().size()));
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

    @FXML
    public void addCourier() {
        CityMap citymap = this.dataModel.getCityMap();
        if(citymap != null) {
           citymap.addDeliveryTour();
            int nbCourier = citymap.getDeliveryTours().size();
            this.nbCourierText.setText(Integer.toString(nbCourier));
        }
    }

    @FXML
    public void deleteCourier() {
        CityMap citymap = this.dataModel.getCityMap();
        if(citymap != null) {
            int nbCourier = citymap.getDeliveryTours().size();
            if(nbCourier > 1){
                try {
                    citymap.deleteDeliveryTour();
                    nbCourier = citymap.getDeliveryTours().size();
                    this.nbCourierText.setText(Integer.toString(nbCourier));
                } catch (NoCourierUnavailableException e) {

                }
            }
        }

    }

    /**
     * Close the currently open "Intersection info" pop-up.
     */
    @FXML
    protected void hideIntersectionInfoDialog() {
        intersectionInfoDialog.setVisible(false);
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
        deliveryWindow.setText("Delivery Window : " + MapController.currentDeliveryRequests.get(MapController.currentIndex).getStartTimeWindow() + "h-"
                + (MapController.currentDeliveryRequests.get(MapController.currentIndex).getStartTimeWindow() + 1) + "h\n"
        );
        if(MapController.currentIndex == MapController.currentDeliveryRequests.size() - 1) {
            nextDeliveryPointInfo.setVisible(false);
        }
        System.out.println("Next");
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
        deliveryWindow.setText("Delivery Window : " + MapController.currentDeliveryRequests.get(MapController.currentIndex).getStartTimeWindow() + "h-"
                + (MapController.currentDeliveryRequests.get(MapController.currentIndex).getStartTimeWindow() + 1) + "h\n"
        );
        if(MapController.currentIndex == 0) {
            prevDeliveryPointInfo.setVisible(false);
        }
        System.out.println("Prev");
    }

    /**
     * Display a pop-up when the user clicks on an intersection / delivery point.
     * If they are some delivery request at this intersection, they are displayed.
     * The user can also use this pop-up to create a new delivery request.
     */
    @FXML
    protected void addDeliveryPointDialog() {
        newDeliveryRequestDialogPane.setVisible(true);
        int start_hour = 8;
        int limit_hour = 12;
        timeWindows.clear();

        courierChoiceBox.getItems().clear();
        timeWindowChoiceBox.getItems().clear();

        System.out.println(dataModel);
        System.out.println(dataModel.getCityMap());
        System.out.println(dataModel.getCityMap().getDeliveryTours());

        for (DeliveryTour deliveryTour : dataModel.getCityMap().getDeliveryTours().values()) {
            courierChoiceBox.getItems().add(deliveryTour.getIdCourier());
        }

        for (int hour = start_hour; hour + 1 <= limit_hour; hour++) {
            String start_am_or_pm = hour <= 11 ? "am" : "pm";
            String end_am_or_pm = (hour + 1) <= 11 ? "am" : "pm";
            String timeWindowText = hour + ".00 " + start_am_or_pm + " - " + (hour + 1) + ".00 " + end_am_or_pm;
            timeWindowChoiceBox.getItems().add(timeWindowText);
            timeWindows.put(timeWindowText, hour);
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
     * Get the data from the currently open "Add new delivery request pop-up" and add the delivery request
     * to the selected courier.
     */
    @FXML
    protected void addDeliveryRequest() {
        DeliveryService deliveryService = DeliveryService.getInstance();
        DeliveryTour deliveryTour = dataModel.getCityMap().getDeliveryTours().get(courierChoiceBox.getValue());
        DeliveryRequest deliveryRequest = new DeliveryRequest(timeWindows.get(timeWindowChoiceBox.getValue()), MapController.currentlySelectedIntersection);
        deliveryTour.addDeliveryRequest(deliveryRequest);
        Text noRouteFoundText = (Text) mapPane.getScene().lookup("#noRouteFound");
        try {
            deliveryService.searchOptimalDeliveryTour(deliveryTour);
            // Draw the delivery tour on the map
            MapController MapController = new MapController();
            MapController.displayDeliveryTour(mapPane, dataModel.getCityMap(), deliveryTour);

            closeAddDeliveryRequestDialogPane();
            intersectionInfoDialog.setVisible(false);
        } catch (IllegalStateException e) {
            deliveryTour.removeDeliveryRequest(deliveryRequest);
            noRouteFoundText.setVisible(true);
        }

    }
}