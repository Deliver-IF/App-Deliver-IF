package com.deliverif.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import lombok.Getter;

import javafx.event.*;
import javafx.scene.control.Button;

@Getter
public class MainController {
    @FXML
    private Button closeNewDeliveryRequestDialogButton;
    @FXML
    private Pane mapPane;

    @FXML
    public void handleButtonPress(ActionEvent event) {
        closeNewDeliveryRequestDialogButton.getParent().getParent().setVisible(false);
    }
}