package com.deliverif.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public class MainController {
    @FXML
    private Pane mapPane;
    @FXML
    private DialogPane intersectionInfoDialog;

    @FXML
    protected void hideIntersectionInfoDialog() {
        intersectionInfoDialog.setVisible(false);
    }

    @FXML
    protected void nextDeliveryPointInfoDialog() {
        System.out.println("Next");
    }

    @FXML
    protected void prevDeliveryPointInfoDialog() {
        System.out.println("Prev");
    }

    @FXML
    protected void addDeliveryPointDialog() {
        System.out.println("Add");
    }

}