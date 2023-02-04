package com.example.cpuschedulersimulator.process.ui;

import com.example.cpuschedulersimulator.process.Job;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddProcessController {
    public Label processNumber;
    public TextField arriveFiled;
    public TextField burstFiled;

    public void setJob(Job job) {
        processNumber.setText("Process " + job.jobNo + " :");

        arriveFiled.textProperty().addListener((observableValue, s, t1) -> {
            String text = observableValue.getValue();

            if (!text.isEmpty())
                try {
                    int arrive = Integer.parseInt(text);
                    job.setArrivalTime(arrive);
                } catch (Exception e) {
                    job.setArrivalTime(0);
                    arriveFiled.setText("0");
                }
        });

        burstFiled.textProperty().addListener((observableValue, s, t1) -> {
            String text = observableValue.getValue();

            if (!text.isEmpty())
                try {
                    int arrive = Integer.parseInt(text);
                    job.setBurstTime(arrive);
                } catch (Exception e) {
                    job.setBurstTime(0);
                    burstFiled.setText("0");
                }
        });
    }
}
