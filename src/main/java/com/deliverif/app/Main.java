package com.deliverif.app;

import com.deliverif.app.models.map.CityMap;
import com.deliverif.app.services.MapFactory;
import com.deliverif.app.vues.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    public static void main(String[] args) {
        System.out.println("Hello world!");
/*        CityMap cm;
        try {
            URL res = Main.class.getClassLoader().getResource("maps/largeMap.xml");
            assert res != null;
            cm = MapFactory.createMapFromPathFile(res.getPath());
            new MainWindow(cm);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }*/

        launch();

    }

    @Override
    public void start(Stage stage) throws Exception {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
    }
}