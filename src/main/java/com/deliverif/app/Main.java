package com.deliverif.app;

import com.deliverif.app.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
    }

    public static void main(String[] args) {
        launch();
    }
}