package com.example.cpuschedulersimulator.process;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CurrentProcess {
    public ObservableList<Job> tableData = FXCollections.observableArrayList();
    public Job currentJob;

    public ObservableList<Job> getTableData() {
        return tableData;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setTableData(ObservableList<Job> tableData) {
        this.tableData = tableData;
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }
}
