package com.example.cpuschedulersimulator.memory.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MemoryPartitionController {


    public VBox root;
    public Label locationLable;
    public Label processName;
    public VBox labelBox;


    public void init(int location, int size, String name) {
        processName.setText(name);
        locationLable.setText(String.valueOf(location));

        root.setPrefHeight(size);
        labelBox.prefHeightProperty().bind(root.prefHeightProperty());
    }
}
