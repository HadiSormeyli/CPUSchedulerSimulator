package com.example.cpuschedulersimulator.process.ui;

import com.example.cpuschedulersimulator.CPU;
import com.example.cpuschedulersimulator.process.CurrentProcess;
import com.example.cpuschedulersimulator.process.Job;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessController {

    public Button startButton;
    public Button stopButton;
    public Button resetButton;
    public Label currentJob;
    public Label currentTime;
    public Label waitingTime;
    public Label turnaroundTime;
    public Label throughput;
    public HBox addProcessRoot;
    public GridPane gridList;
    public Button addProcess;
    public Button cancelButton;
    public Button okButton;
    public Button setProcess;
    public TableView<Job> table;
    public HBox gantChartLayout;
    @FXML
    private RadioButton fcfs;
    @FXML
    private RadioButton sjf;
    @FXML
    private RadioButton srtf;
    @FXML
    private RadioButton rr;
    @FXML
    private ToggleGroup group;
    @FXML
    private TextField quantumFiled;
    @FXML
    private Label quantumLabel;

    private final ObservableList<Job> jobs = FXCollections.observableArrayList();
    private int algorithm = 0;
    private boolean allJobsDone = false;
    private boolean stop = false;
    private int time = 0;


    private void addProcess() {
        jobs.clear();
        gridList.getChildren().clear();

        AtomicInteger row = new AtomicInteger(1);

        addProcess.setOnAction(actionEvent -> {
            row.getAndIncrement();
            setUpAddProcessLayout(row.get());
        });

        okButton.setOnAction(actionEvent -> {
            addProcessRoot.setVisible(false);
            enableButtons();

            table.setItems(jobs);
            table.refresh();
        });

        setUpAddProcessLayout(0);
        setUpAddProcessLayout(1);
    }

    private void disableButtons() {
        startButton.setDisable(true);
        stopButton.setDisable(true);
        resetButton.setDisable(true);
        setProcess.setDisable(false);
    }

    private void enableButtons() {
        startButton.setDisable(false);
        stopButton.setDisable(false);
        resetButton.setDisable(false);
        setProcess.setDisable(true);
    }

    private void setUpAddProcessLayout(int row) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/cpuschedulersimulator/add-process-view.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddProcessController addProcessController = fxmlLoader.getController();
            Job job = new Job(row + 1);
            jobs.add(row, job);
            addProcessController.setJob(job);
            gridList.add(root, 0, row);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void init() {

        startButton.setOnAction(actionEvent -> {
            startButton.setDisable(true);
            startSimulate();
        });

        stopButton.setOnAction(actionEvent -> {
            stop = !stop;
            if (stop) stopButton.setText("Resume");
            else stopButton.setText("Stop");
        });


        resetButton.setOnAction(actionEvent -> {
            jobs.clear();
            table.setItems(null);
            table.refresh();
            stop = false;
            stopButton.setText("Stop");
            disableButtons();
            currentJob.setText("Current Job: ");
            currentTime.setText("Current time: ");
            waitingTime.setText("Average waiting time: ");
            turnaroundTime.setText("Average turnaround time:");
            throughput.setText("Throughput: ");
            gantChartLayout.getChildren().clear();
        });

        disableButtons();
        setUpTable();
        CPU.setJobList(jobs);

        group.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (group.getSelectedToggle() != null) {
                RadioButton selected = (RadioButton) group.getSelectedToggle();
                quantumLabel.setDisable(rr != selected);
                quantumFiled.setDisable(rr != selected);
            }
        });
    }

    private void setUpTable() {
        TableColumn<Job, String> number = new TableColumn<>("Number");
        number.setCellValueFactory(new PropertyValueFactory<>("jobNo"));

        TableColumn<Job, String> arriveTime = new TableColumn<>("Arrive Time");
        arriveTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));

        TableColumn<Job, String> burstTime = new TableColumn<>("Burst Time");
        burstTime.setCellValueFactory(new PropertyValueFactory<>("burstTime"));

        TableColumn<Job, String> startTime = new TableColumn<>("Start Time");
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<Job, String> waitTime = new TableColumn<>("Wait Time");
        waitTime.setCellValueFactory(new PropertyValueFactory<>("waitTime"));

        TableColumn<Job, String> remainingTime = new TableColumn<>("Remaining Time");
        remainingTime.setCellValueFactory(new PropertyValueFactory<>("remainingTime"));

        TableColumn<Job, String> finishTime = new TableColumn<>("Finish Time");
        finishTime.setCellValueFactory(new PropertyValueFactory<>("finishedTime"));

        TableColumn<Job, String> turnaround = new TableColumn<>("Turnaround");
        turnaround.setCellValueFactory(new PropertyValueFactory<>("turnAroundTime"));

        table.getColumns().addAll(number, arriveTime, burstTime, startTime, waitTime, remainingTime, finishTime, turnaround);
    }

    @FXML
    private void setProcessAction(ActionEvent actionEvent) {
        actionEvent.consume();
        addProcessRoot.setVisible(true);
        addProcess();
    }

    public void cancelButtonAction(ActionEvent actionEvent) {
        actionEvent.consume();
        addProcessRoot.setVisible(false);
    }

    private int getSelectedAlgorithm() {
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        if (fcfs == selected) {
            return 0;
        } else if (sjf == selected) {
            return 1;
        } else if (srtf == selected) {
            return 2;
        } else {
            return 3;
        }
    }

    private void nextStep() {
        CurrentProcess currentProcess = CPU.nextProcessStep(time);

        Platform.runLater(() -> {
            table.setItems(currentProcess.tableData);
            table.refresh();
            Job currentJob = currentProcess.currentJob;

            currentTime.setText("Current time: " + time);

            if (currentJob.getJobNo() != -1) {
                this.currentJob.setText("Current Job: job " + currentJob.getJobNo());
            } else {
                this.currentJob.setText("waiting");
            }

            if (currentJob.getJobNo() == 0) {
                allJobsDone = true;
                calculateAverages(currentProcess.tableData);
                stop = false;
                stopButton.setText("Stop");
                startButton.setDisable(false);
                stopButton.setDisable(false);
                resetButton.setDisable(false);
                setProcess.setDisable(false);
            } else {
                GantChartCell cell = new GantChartCell(currentJob.jobNo, currentJob.getColor());
                gantChartLayout.getChildren().add(cell);
            }
            time++;
        });
    }

    private void calculateAverages(ObservableList<Job> jobs) {
        DecimalFormat formatter = new DecimalFormat("##.###");

        double totalWaitTime = 0;
        double totalTurnaroundTime = 0;
        double finishTime = 0;

        for (Job job : jobs)
            totalWaitTime = (totalWaitTime + job.waitTime);

        waitingTime.setText("Average waiting time: " + formatter.format(totalWaitTime / jobs.size()));

        for (Job job : jobs) {
            totalTurnaroundTime = (totalTurnaroundTime + job.turnAroundTime);
            turnaroundTime.setText("Average turnaround time: " + formatter.format(totalTurnaroundTime / jobs.size()));
        }

        for (Job job : jobs)
            if (job.finishedTime > finishTime)
                finishTime = job.finishedTime;

        throughput.setText("Throughput: " + formatter.format(jobs.size() / finishTime));
    }


    private void startSimulate() {
        allJobsDone = false;
        time = 0;
        stop = false;
        stopButton.setText("Stop");
        algorithm = getSelectedAlgorithm();
        CPU.setProcessAlgorithm(algorithm);
        CPU.setQuantum(Integer.parseInt(quantumFiled.getText()));
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                while (!allJobsDone) {
                    if (!stop) {
                        try {
                            nextStep();
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return null;
            }
        };


        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}