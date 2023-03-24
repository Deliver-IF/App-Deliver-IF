package com.deliverif.app;

import com.deliverif.app.controller.MainController;
import com.deliverif.app.model.CityMap;
import com.deliverif.app.model.Intersection;
import com.deliverif.app.model.Segment;
import com.deliverif.app.services.MapFactory;
import com.deliverif.app.controller.MapController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.awt.Dimension;


public class Main extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Set main window size
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth()-screenSize.getWidth()*0.2;
        double height = screenSize.getHeight()-screenSize.getHeight()*0.2;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/main.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, width, height);

        primaryStage.setScene(scene);
        primaryStage.setTitle("DELIVR'IF");
        primaryStage.show();

        MainController mainController = fxmlLoader.getController();
        System.out.println(mainController.getCourier1streets().toString());
    }

    public static void main(String[] args) {
        launch();
    }
}