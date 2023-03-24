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


        BaseMap baseMap = new BaseMap();
        cityMap.addDeliveryTourTest(new DeliveryTour(cityMap));
        baseMap.drawBasemap(controller.getMapPane(), cityMap);

        stage.setTitle("DELIVR'IF");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}