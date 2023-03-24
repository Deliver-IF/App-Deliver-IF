package com.deliverif.app.controller;

import com.deliverif.app.Main;
import com.deliverif.app.model.CityMap;
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

    CityMap cityMap;

    ArrayList<Segment> courier1streets = new ArrayList<Segment>();
    ArrayList<Intersection> courier1deliverypoints = new ArrayList<Intersection>();

    MapController map = new MapController();

    public MainController() {

    }

    // Method called by the FXML loader after ressource file reading
    public void initialize() {
        dummyFunction();
        System.out.println(courier1streets.toString());
    }

    @FXML
    public void loadMapAction() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(menuBar.getScene().getWindow());
        if (file != null) {
/*            try {
                ;
            } catch (IOException exc) {
                // handle exception...
            }*/
        }
    }

    public void dummyFunction() {
        // TODO: Move to model, nothing showing because it's during init... so the windows isn't opened yet and the pane size = 0
        try {
            URL res = Main.class.getResource("maps/smallMap.xml");
            assert res != null;
            cityMap = MapFactory.createMapFromPathFile(URLDecoder.decode(res.getPath(), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Intersection intersection1 = new Intersection("25336175", (float) 4.863642, (float) 45.75301);
        Intersection intersection2 = new Intersection("25336178", (float) 4.8658633, (float) 45.752552);
        Intersection intersection3 = new Intersection("195270", (float) 4.865392, (float) 45.751556);
        Intersection intersection4 = new Intersection("522100855", (float) 4.866085, (float) 45.751476);
        Intersection intersection5 = new Intersection("1345284783", (float) 4.8660784, (float) 45.752106);

        Segment segJeanRenoir = new Segment("Rue Jean Renoir",
                (float) 179.80255,
                intersection1,
                intersection2
        );
        Segment segRueDeDauphine = new Segment("Rue du Dauphiné",
                (float) 117.45386,
                intersection2,
                intersection3
        );
        Segment segRueRogerBrechan = new Segment("Rue Roger Brechan",
                (float) 54.50388,
                intersection3,
                intersection4
        );
        Segment segClaudeRampon = new Segment("Imp. Claude Rampon",
                (float) 70.188835,
                intersection4,
                intersection5
        );

        courier1streets.add(segJeanRenoir);
        courier1streets.add(segRueDeDauphine);
        courier1streets.add(segRueRogerBrechan);
        courier1streets.add(segClaudeRampon);
        courier1deliverypoints.add(intersection1);
        courier1deliverypoints.add(intersection2);
        courier1deliverypoints.add(intersection5);

        map.drawBasemap(this.getMapPane(), cityMap);

        map.displayCourierPath(this.getMapPane(), cityMap, courier1streets, courier1deliverypoints);
    }
}