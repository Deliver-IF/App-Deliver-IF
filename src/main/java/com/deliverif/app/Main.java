package com.deliverif.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private double screenWidth, screenHeight;

    @Override
    public void init() throws Exception {
        super.init();

        // Set main window size
        screenWidth = Screen.getPrimary().getBounds().getWidth()*0.8;
        screenHeight = Screen.getPrimary().getBounds().getHeight()*0.8;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/main.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, screenWidth, screenHeight);

        primaryStage.setScene(scene);
        primaryStage.setTitle("DELIVR'IF");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}