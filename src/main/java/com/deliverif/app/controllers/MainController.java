package com.deliverif.app.controllers;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.DeliveryTour;
import com.deliverif.app.services.DeliveryService;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import lombok.Getter;

import javafx.event.*;
import javafx.scene.control.Button;
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
    public void handleButtonPress(ActionEvent event) {
        closeNewDeliveryRequestDialogButton.getParent().getParent().setVisible(false);

        Map<Integer, DeliveryTour> deliveryTourMap =  cityMap.getDeliveryTours();

        DeliveryService deliveryService = DeliveryService.getInstance();
        deliveryService.searchOptimalDeliveryTour(deliveryTourMap.get(0));
    }
}