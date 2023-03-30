package com.deliverif.app;

import com.deliverif.app.controller.MainController;
import com.deliverif.app.utils.Constants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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
        MainController mainController = fxmlLoader.getController();

        // Event handler for the window size change event
        final ChangeListener<Number> listener = new ChangeListener<Number>()
        {
            final Timer timer = new Timer(true);
            TimerTask task = null;
            final long delayTime = 200;

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue) {
                if (task != null) {
                    task.cancel();
                }
                task = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> mainController.onWindowSizeChangeEventHandler());
                    }
                };
                timer.schedule(task, delayTime);
            }
        };

        primaryStage.widthProperty().addListener(listener);
        primaryStage.heightProperty().addListener(listener);

        primaryStage.setScene(scene);
        primaryStage.setTitle(Constants.APP_NAME);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}