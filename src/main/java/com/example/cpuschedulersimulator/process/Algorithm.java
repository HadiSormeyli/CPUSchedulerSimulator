package com.example.cpuschedulersimulator.process;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.example.cpuschedulersimulator.process.Job.*;


public abstract class Algorithm {
    protected ObservableList<Job> tempQueue;
    protected ObservableList<Job> readyQueue = FXCollections.observableArrayList();
    protected CurrentProcess currentProcess = new CurrentProcess();
    ObservableList<Job> jobList;

    public Algorithm(ObservableList<Job> jobList) {
        currentProcess.setTableData(arrayListCopy(jobList));
        this.jobList = sortByArrivalTime(arrayListCopy(jobList));
        this.tempQueue = arrayListCopy(this.jobList);
    }

    public abstract CurrentProcess nextStep(int currentTime);

    public void addArrivedToTempQ(int currentTime) {
        if (!tempQueue.isEmpty()) {
            tempQueue.forEach(job -> {
                if (job.arrivalTime == currentTime) {
                    readyQueue.add(job.getCopy());
                }

            });
            readyQueue.forEach(job -> {
                tempQueue.remove(job);
            });
        }
    }

    public ObservableList<Job> sortByArrivalTime(ObservableList<Job> list) {
        list.sort(arrivalTimeComparator);
        return list;
    }

    public ObservableList<Job> sortByBurstTime(ObservableList<Job> list) {
        list.sort(burstTimeComparator);
        return list;
    }

    public ObservableList<Job> sortByRemainingTime(ObservableList<Job> list) {
        list.sort(remainingTimeComparator);
        return list;
    }

    public ObservableList<Job> arrayListCopy(ObservableList<Job> list) {
        ObservableList<Job> copy = FXCollections.observableArrayList();

        list.forEach(job -> {
            copy.add(job.getCopy());
        });

        return copy;
    }
}
