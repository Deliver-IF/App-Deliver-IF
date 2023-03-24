package com.deliverif.app.controllers;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.DeliveryRequest;
import com.deliverif.app.models.map.DeliveryTour;
import com.deliverif.app.services.DeliveryService;
import com.deliverif.app.views.BaseMap;
import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Getter;

import javafx.event.*;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MainController {
    @FXML
    private DialogPane newDeliveryRequestDialogPane;
    @FXML
    private Pane mapPane;

    @Setter
    private CityMap cityMap;

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

    @FXML
    protected void hideIntersectionInfoDialog() {
        intersectionInfoDialog.setVisible(false);
    }

    @FXML
    protected void nextDeliveryPointInfoDialog() {
        if(BaseMap.currentIndex == 0) {
            prevDeliveryPointInfo.setVisible(true);
        }
        BaseMap.currentIndex++;
        deliveryWindow.setText("Delivery Window : " + BaseMap.currentDeliveryRequests.get(BaseMap.currentIndex).getStartTimeWindow() + "h-"
                + (BaseMap.currentDeliveryRequests.get(BaseMap.currentIndex).getStartTimeWindow() + 1) + "h\n"
        );
        if(BaseMap.currentIndex == BaseMap.currentDeliveryRequests.size() - 1) {
            nextDeliveryPointInfo.setVisible(false);
        }
        System.out.println("Next");
    }

    @FXML
    protected void prevDeliveryPointInfoDialog() {
        if(BaseMap.currentIndex == BaseMap.currentDeliveryRequests.size() - 1) {
            nextDeliveryPointInfo.setVisible(true);
        }
        BaseMap.currentIndex--;
        deliveryWindow.setText("Delivery Window : " + BaseMap.currentDeliveryRequests.get(BaseMap.currentIndex).getStartTimeWindow() + "h-"
                + (BaseMap.currentDeliveryRequests.get(BaseMap.currentIndex).getStartTimeWindow() + 1) + "h\n"
        );
        if(BaseMap.currentIndex == 0) {
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

        for (DeliveryTour deliveryTour : cityMap.getDeliveryTours().values()) {
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
        DeliveryTour deliveryTour = cityMap.getDeliveryTours().get(courierChoiceBox.getValue());
        DeliveryRequest deliveryRequest = new DeliveryRequest(timeWindows.get(timeWindowChoiceBox.getValue()), BaseMap.currentlySelectedIntersection);
        deliveryTour.addDeliveryRequest(deliveryRequest);
        deliveryService.searchOptimalDeliveryTour(deliveryTour);
        closeAddDeliveryRequestDialogPane();
        BaseMap baseMap = new BaseMap();
        baseMap.displayDeliveryTour(mapPane, cityMap, deliveryTour);
    }
}