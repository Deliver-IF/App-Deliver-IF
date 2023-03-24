package com.deliverif.app.controller;

import com.deliverif.app.model.DataModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import javafx.stage.FileChooser;
import lombok.Getter;

import java.io.File;

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
        this.dataModel.getMapController().drawBasemap(this.mapPane, this.dataModel.getCityMap());
    }
}