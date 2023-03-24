package com.deliverif.app.controllers;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.DeliveryTour;
import com.deliverif.app.services.DeliveryService;
import com.deliverif.app.views.BaseMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Getter;

import javafx.event.*;
import lombok.Setter;

import java.util.Map;

@Getter
public class MainController {
    @FXML
    private Button closeNewDeliveryRequestDialogButton;
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
    public void handleButtonPress(ActionEvent event) {
        closeNewDeliveryRequestDialogButton.getParent().getParent().setVisible(false);

        Map<Integer, DeliveryTour> deliveryTourMap =  cityMap.getDeliveryTours();

        DeliveryService deliveryService = DeliveryService.getInstance();
        deliveryService.searchOptimalDeliveryTour(deliveryTourMap.get(0));
    }

    @FXML
    protected void addDeliveryPointDialog() {
        System.out.println("Add");
    }
}