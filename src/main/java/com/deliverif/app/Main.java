package com.deliverif.app;

import com.deliverif.app.controllers.MainController;
import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.models.map.Intersection;
import com.deliverif.app.models.map.Segment;
import com.deliverif.app.services.MapFactory;
import com.deliverif.app.views.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 800);
        stage.setScene(scene);
        stage.show();
        MainController controller = fxmlLoader.getController();

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

        ArrayList<Segment> courier1streets = new ArrayList<Segment>();
        ArrayList<Intersection> courier1deliverypoints = new ArrayList<Intersection>();
        courier1streets.add(segJeanRenoir);
        courier1streets.add(segRueDeDauphine);
        courier1streets.add(segRueRogerBrechan);
        courier1streets.add(segClaudeRampon);
        courier1deliverypoints.add(intersection1);
        courier1deliverypoints.add(intersection2);
        courier1deliverypoints.add(intersection5);

        ArrayList<Segment> courier2streets = new ArrayList<Segment>();
        ArrayList<Intersection> courier2deliverypoints = new ArrayList<Intersection>();
        Segment originSegment = cityMap.getSegments().get(15);

        Pair<Intersection, Segment> currentPair = null;
        for (int i = 0; i < 9; i++) {
            currentPair = originSegment.getDestination().getReachableIntersections().get(0);
            if (currentPair.getRight() == originSegment) {
                currentPair = originSegment.getDestination().getReachableIntersections().get(1);
            }

            courier2streets.add(currentPair.getRight());
            originSegment = currentPair.getRight();
        }
        courier2deliverypoints.add(originSegment.getOrigin());
        // ------------------- //

        Map map = new Map();
        map.drawBasemap(controller.getMapPane(), cityMap);

        map.displayCourierPath(controller.getMapPane(), cityMap, courier1streets, courier1deliverypoints);
        map.displayCourierPath(controller.getMapPane(), cityMap, courier2streets, courier2deliverypoints);

        stage.setTitle("Deliver'IF");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}