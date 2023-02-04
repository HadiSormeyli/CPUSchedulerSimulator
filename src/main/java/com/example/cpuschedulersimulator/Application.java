package com.example.cpuschedulersimulator;

import com.example.cpuschedulersimulator.memory.ui.MemoryController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("memory-view.fxml"));
        Parent root = fxmlLoader.load();
//        ProcessController processController = fxmlLoader.getController();
//        processController.init();
        ((MemoryController) fxmlLoader.getController()).init();
        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}