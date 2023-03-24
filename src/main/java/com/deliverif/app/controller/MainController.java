package com.deliverif.app.controller;

import com.deliverif.app.Main;
import com.deliverif.app.model.CityMap;
import com.deliverif.app.model.DataModel;
import com.deliverif.app.model.Intersection;
import com.deliverif.app.model.Segment;
import com.deliverif.app.services.MapFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import javafx.stage.FileChooser;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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

    ArrayList<Segment> courier1streets = new ArrayList<Segment>();
    ArrayList<Intersection> courier1deliverypoints = new ArrayList<Intersection>();

    MapController map = new MapController();

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