package com.deliverif.app;

import com.deliverif.app.controllers.MainController;
import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.services.MapFactory;
import com.deliverif.app.views.Map;
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


public class Main extends Application {
    CityMap cityMap;

    @Override
    public void start(Stage stage) throws IOException {
        // Load map
        try {
            URL res = Main.class.getResource("maps/largeMap.xml");
            assert res != null;
            cityMap = MapFactory.createMapFromPathFile(URLDecoder.decode(res.getPath(), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Load UI
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth()-screenSize.getWidth()*0.2;
        double height = screenSize.getHeight()-screenSize.getHeight()*0.2;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setScene(scene);
        stage.show();
        MainController controller = fxmlLoader.getController();
        Map.draw(controller.getMapPane(), cityMap);
        stage.setTitle("DELIVR'IF");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}