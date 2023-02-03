package com.example.cpuschedulersimulator;

import com.example.cpuschedulersimulator.process.ui.ProcessController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("process-view.fxml"));
        Parent root = fxmlLoader.load();
        ProcessController processController = fxmlLoader.getController();
        processController.init();
        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}