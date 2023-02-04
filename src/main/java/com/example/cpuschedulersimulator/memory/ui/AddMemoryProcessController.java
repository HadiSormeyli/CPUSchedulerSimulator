package com.example.cpuschedulersimulator.memory.ui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.example.cpuschedulersimulator.memory.Process;

public class AddMemoryProcessController {
    public Label processNumber;
    public TextField sizeFiled;
    public TextField arriveFiled;
    public TextField endFiled;


    public void setProcess(Process process) {

        processNumber.setText("Process " + process.getNumber() + " :");

        arriveFiled.textProperty().addListener((observableValue, s, t1) -> {
            String text = observableValue.getValue();

            if (!text.isEmpty())
                try {
                    int arrive = Integer.parseInt(text);
                    process.setStart_time(arrive);
                } catch (Exception e) {
                    process.setStart_time(0);
                    arriveFiled.setText("0");
                }
        });

        endFiled.textProperty().addListener((observableValue, s, t1) -> {
            String text = observableValue.getValue();

            if (!text.isEmpty())
                try {
                    int end = Integer.parseInt(text);
                    process.setEnd_time(end);
                } catch (Exception e) {
                    process.setEnd_time(0);
                    endFiled.setText("0");
                }
        });

        sizeFiled.textProperty().addListener((observableValue, s, t1) -> {
            String text = observableValue.getValue();

            if (!text.isEmpty())
                try {
                    int size = Integer.parseInt(text);
                    process.setSize(size);
                } catch (Exception e) {
                    process.setSize(0);
                    sizeFiled.setText("0");
                }
        });
    }
}
