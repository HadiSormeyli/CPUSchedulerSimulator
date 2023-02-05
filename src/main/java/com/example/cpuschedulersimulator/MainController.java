package com.example.cpuschedulersimulator;

import com.example.cpuschedulersimulator.memory.ui.MemoryController;
import com.example.cpuschedulersimulator.process.ui.ProcessController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

import static com.example.cpuschedulersimulator.Application.stage;

public class MainController {


    public Button processManagement;
    public Button memoryManagement;

    private void showMemorySimulator() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("memory-view.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((MemoryController) fxmlLoader.getController()).init();
        Scene scene = new Scene(root, 770, 500);
        stage.setTitle("Memory");
        stage.setScene(scene);
        stage.show();
    }

    private void showProcessSimulator() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("process-view.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((ProcessController) fxmlLoader.getController()).init();
        Scene scene = new Scene(root, 700, 400);
        stage.setTitle("Process");
        stage.setScene(scene);
        stage.show();
    }

    public void onMemoryManagement(ActionEvent actionEvent) {
        showMemorySimulator();
    }

    public void onProcessManagement(ActionEvent actionEvent) {
        showProcessSimulator();
    }
}
