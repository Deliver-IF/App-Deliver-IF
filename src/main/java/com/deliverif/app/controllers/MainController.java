package com.deliverif.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public class MainController {
    @FXML
    private Label welcomeText;
    @FXML
    private Pane mapPane;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


}