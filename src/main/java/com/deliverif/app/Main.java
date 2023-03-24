package com.deliverif.app;

import com.deliverif.app.controllers.MainController;
import com.deliverif.app.models.map.*;
import com.deliverif.app.services.MapFactory;
import com.deliverif.app.views.BaseMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;


public class Main extends Application {
    CityMap cityMap;

    @Override
    public void start(Stage stage) throws IOException {
        // Load map
        try {
            URL res = Main.class.getResource("maps/smallMap.xml");
            assert res != null;
            cityMap = MapFactory.createMapFromPathFile(URLDecoder.decode(res.getPath(), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Load UI
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth()-screenSize.getWidth()*0.2;
        double height = screenSize.getHeight()-screenSize.getHeight()*0.2;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/add_delivery_request.fxml"));

        Scene defaultScene = new Scene(fxmlLoader.load(), width, height);
        stage.setScene(defaultScene);
        stage.show();
        MainController controller = fxmlLoader.getController();
        controller.setCityMap(this.cityMap);

        // ---- Examples ----- //

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

        DeliveryRequest dr10 = new DeliveryRequest(
          8,
          intersection1
        );
        DeliveryRequest dr20 = new DeliveryRequest(
          9,
          intersection2
        );
        DeliveryRequest dr30 = new DeliveryRequest(
          10,
          intersection3
        );
        DeliveryRequest dr40 = new DeliveryRequest(
          10,
          intersection4
        );
        DeliveryRequest dr50 = new DeliveryRequest(
          10,
          intersection5
        );
        DeliveryRequest dr51 = new DeliveryRequest(
          14,
          intersection5
        );

        DeliveryTour dt1 = new DeliveryTour(cityMap);
        dt1.addDeliveryRequest(dr10);
        dt1.addDeliveryRequest(dr20);
        dt1.addDeliveryRequest(dr50);

        dt1.addTour(segJeanRenoir);
        dt1.addTour(segRueDeDauphine);
        dt1.addTour(segRueRogerBrechan);
        dt1.addTour(segClaudeRampon);

        DeliveryTour dt2 = new DeliveryTour(cityMap);
        dt2.addDeliveryRequest(dr40);
        dt2.addDeliveryRequest(dr51);

        dt2.addTour(segRueRogerBrechan);
        dt2.addTour(segClaudeRampon);

        Set<Intersection> courier1deliverypoints = new HashSet<>();

        // ------------------- //

        BaseMap baseMap = new BaseMap();
        cityMap.addDeliveryTourTest(dt1);
        cityMap.addDeliveryTourTest(dt2);
        baseMap.drawBasemap(controller.getMapPane(), cityMap);

        baseMap.displayDeliveryTour(controller.getMapPane(), cityMap, dt2);

        stage.setTitle("DELIVR'IF");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}