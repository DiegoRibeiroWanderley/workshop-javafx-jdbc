package com.diegoribeiro.javafxws;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException{

            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("View.fxml")));
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
    }
}
