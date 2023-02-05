package com.example.cpuschedulersimulator.memory.ui;

import com.example.cpuschedulersimulator.CPU;
import com.example.cpuschedulersimulator.memory.Process;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.cpuschedulersimulator.memory.MemorySimulator.OUT_OF_MEMORY;

public class MemoryController {
    public Button setProcess;
    public Button addProcess;
    public Label memorySize;
    public Label osSize;
    public ToggleGroup group;
    public RadioButton firstFit;
    public RadioButton bestFit;
    public RadioButton worstFit;
    public RadioButton nextFit;
    public TableView<Process> table;
    public TableView<Process> freeTable;
    public VBox memoryStatusLayout;
    public Button nextStepButton;
    public HBox addProcessRoot;
    public GridPane gridList;
    public Button okButton;
    public Button cancelButton;
    public TextField memorySizeFiled;
    public TextField osSizeFiled;

    public Label eventLabel;
    private final ObservableList<Process> processes = FXCollections.observableArrayList();


    public void init() {
        setProcess.setOnAction(actionEvent -> {
            actionEvent.consume();
            table.setItems(null);
            table.refresh();

            freeTable.setItems(null);
            freeTable.refresh();

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

        setUpTable();
        setUpFreeTable();
    }

    private void setUpFreeTable() {
        TableColumn<Process, String> number = new TableColumn<>("Number");
        number.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Process, String> arriveTime = new TableColumn<>("Size");
        arriveTime.setCellValueFactory(new PropertyValueFactory<>("size"));

        TableColumn<Process, String> waitTime = new TableColumn<>("Location");
        waitTime.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Process, String> remainingTime = new TableColumn<>("Status");
        remainingTime.setCellValueFactory(new PropertyValueFactory<>("status"));


        freeTable.getColumns().addAll(number, arriveTime, waitTime, remainingTime);
    }

    private void setUpTable() {
        TableColumn<Process, String> number = new TableColumn<>("Number");
        number.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Process, String> arriveTime = new TableColumn<>("Size");
        arriveTime.setCellValueFactory(new PropertyValueFactory<>("size"));

        TableColumn<Process, String> burstTime = new TableColumn<>("Start time");
        burstTime.setCellValueFactory(new PropertyValueFactory<>("start_time"));

        TableColumn<Process, String> startTime = new TableColumn<>("End time");
        startTime.setCellValueFactory(new PropertyValueFactory<>("end_time"));

        TableColumn<Process, String> waitTime = new TableColumn<>("Location");
        waitTime.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Process, String> remainingTime = new TableColumn<>("Status");
        remainingTime.setCellValueFactory(new PropertyValueFactory<>("status"));


        table.getColumns().addAll(number, arriveTime, burstTime, startTime, waitTime, remainingTime);
    }

    private int getSelectedAlgorithm() {
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        if (firstFit == selected) {
            return 0;
        } else if (bestFit == selected) {
            return 1;
        } else if (worstFit == selected) {
            return 2;
        } else {
            return 3;
        }
    }

    private void nextStep() {
        if (CPU.memorySimulatorBase.processesRemaining() > 0) {
            ObservableList<Process> list1 = CPU.nextMemoryStep();
            table.setItems(list1);
            table.refresh();

            ObservableList<Process> list2 = CPU.memorySimulatorBase.getFreeSpace();
            freeTable.setItems(list2);
            freeTable.refresh();

            String event = CPU.memorySimulatorBase.getEventText();
            if (event.equals(OUT_OF_MEMORY)) {
                eventLabel.setStyle("-fx-text-fill: red");
            } else {
                eventLabel.setStyle("-fx-text-fill: black");
            }
            eventLabel.setText(event);

            showMemoryStatus(list1, list2);
        }
    }

    private void showMemoryStatus(ObservableList<Process> list1, ObservableList<Process> list2) {
        memoryStatusLayout.getChildren().clear();
        List<Process> newList = Stream.concat(list1.stream().filter(process -> process.getStatus() == Process.Status.ALLOCATED), list2.stream())
                .sorted(Comparator.comparing(Process::getLocation))
                .collect(Collectors.toList());


        addPartition(0, CPU.memorySimulatorBase.getOsSize(), "OS");
        for (Process p : newList) {
            addPartition(p.getLocation(), p.getSize(), ((p.getStatus() == Process.Status.AVAILABLE) ? "F" : "P") + p.getNumber());
        }
        addPartition(CPU.memorySimulatorBase.getMemorySize(), 0, "");
    }

    private void addPartition(int location, int size, String name) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/cpuschedulersimulator/memory-partition-view.fxml"));
        try {
            Parent root = fxmlLoader.load();
            MemoryPartitionController memoryPartitionController = fxmlLoader.getController();
            memoryPartitionController.init(location, size, name);
            memoryStatusLayout.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            memoryStatusLayout.setPrefHeight(memory);
            osSize.setText("OS Size: " + os);
            memorySize.setText("Memory Size: " + memory);
            CPU.setMemoryAlgorithm(getSelectedAlgorithm(), memory, os);

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
