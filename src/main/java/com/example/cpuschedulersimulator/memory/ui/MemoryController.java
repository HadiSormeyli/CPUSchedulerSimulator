package com.example.cpuschedulersimulator.memory.ui;

import com.example.cpuschedulersimulator.CPU;
import com.example.cpuschedulersimulator.memory.Process;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryController {
    public Button resetButton;
    public Button setProcess;
    public Button addProcess;
    public Label memorySize;
    public Label osSize;
    public ToggleGroup group;
    public RadioButton firstFit;
    public RadioButton bestFit;
    public RadioButton worstFit;
    public TableView<Process> table;
    public VBox memoryStatusLayout;
    public Button nextStepButton;
    public HBox addProcessRoot;
    public GridPane gridList;
    public Button okButton;
    public Button cancelButton;
    public TextField memorySizeFiled;
    public TextField osSizeFiled;

    public TextField blocksSize;
    private final ObservableList<Process> processes = FXCollections.observableArrayList();


    public void init() {
        setProcess.setOnAction(actionEvent -> {
            actionEvent.consume();
            addProcessRoot.setVisible(true);
            addProcess();
        });

        cancelButton.setOnAction(actionEvent -> {
            actionEvent.consume();
            addProcessRoot.setVisible(false);
        });

        nextStepButton.setOnAction(actionEvent -> {
            nextStep();
        });
    }

    private int getSelectedAlgorithm() {
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        if (firstFit == selected) {
            return 0;
        } else if (bestFit == selected) {
            return 1;
        } else {
            return 2;
        }
    }

    private void nextStep() {
        if (CPU.memorySimulatorBase.processesRemaining() > 0) CPU.nextMemoryStep();
    }

    public void addProcess() {
        processes.clear();
        gridList.getChildren().clear();

        AtomicInteger row = new AtomicInteger(1);

        addProcess.setOnAction(actionEvent -> {
            row.getAndIncrement();
            setUpAddProcessLayout(row.get());
        });

        okButton.setOnAction(actionEvent -> {
            CPU.setProcessList(processes);
            int memory = Integer.parseInt(memorySizeFiled.getText());
            int os = Integer.parseInt(osSizeFiled.getText());
            int blocks = Integer.parseInt(blocksSize.getText());
            osSize.setText("OS Size: " + os);
            memorySize.setText("Memory Size: " + memory);
            CPU.setMemoryAlgorithm(getSelectedAlgorithm(), memory, os, blocks);

            addProcessRoot.setVisible(false);

            table.setItems(processes);
            table.refresh();
        });

        setUpAddProcessLayout(0);
        setUpAddProcessLayout(1);
    }

    private void setUpAddProcessLayout(int row) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/cpuschedulersimulator/add-memory-process-view.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddMemoryProcessController addMemoryProcessController = fxmlLoader.getController();
            Process process = new Process(row + 1);
            processes.add(row, process);
            addMemoryProcessController.setProcess(process);
            gridList.add(root, 0, row);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
