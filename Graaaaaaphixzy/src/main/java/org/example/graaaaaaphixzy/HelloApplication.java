package org.example.graaaaaaphixzy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import org.example.graaaaaaphixzy.UIStuff.Window_1;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {

        Window_1 window = new Window_1();

        Scene scene = new Scene(window.getRoot(), 1000, 800);

        stage.setTitle("Generator de labirint");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}