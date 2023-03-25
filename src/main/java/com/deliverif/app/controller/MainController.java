package com.deliverif.app.controller;

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

    // Method called by the FXML loader after ressource file reading
    public void initialize() {

    }

    @FXML
    public void loadMapAction() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(menuBar.getScene().getWindow());
        if (file != null) {
           try {
                this.dataModel.loadMapFromFile(file);
            } catch (Exception exc) {
                // handle exception...
            }
        }
        //TODO: controller in model...
        this.dataModel.getMapController().drawBasemap(this.mapPane, this.dataModel.getCityMap());
    }

    @FXML
    protected void hideIntersectionInfoDialog() {
        intersectionInfoDialog.setVisible(false);
    }

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

    @FXML
    protected void closeAddDeliveryRequestDialogPane() {
        newDeliveryRequestDialogPane.setVisible(false);
    }

    @FXML
    protected void addDeliveryRequest() {
        DeliveryService deliveryService = DeliveryService.getInstance();
        DeliveryTour deliveryTour = dataModel.getCityMap().getDeliveryTours().get(courierChoiceBox.getValue());
        DeliveryRequest deliveryRequest = new DeliveryRequest(timeWindows.get(timeWindowChoiceBox.getValue()), MapController.currentlySelectedIntersection);
        deliveryTour.addDeliveryRequest(deliveryRequest);
        deliveryService.searchOptimalDeliveryTour(deliveryTour);
        closeAddDeliveryRequestDialogPane();
        MapController MapController = new MapController();
        MapController.displayDeliveryTour(mapPane, dataModel.getCityMap(), deliveryTour);
    }


}