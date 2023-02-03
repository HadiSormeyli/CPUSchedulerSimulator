package com.example.cpuschedulersimulator.process;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

import static com.example.cpuschedulersimulator.process.Job.*;


public abstract class Algorithm {
    ObservableList<Job> jobList;
    protected ObservableList<Job> tempQueue;
    ObservableList<Job> currentProcessData;
    protected ObservableList<Job> readyQueue = FXCollections.observableArrayList();
    Job currentJob;
    protected CurrentProcess currentProcess = new CurrentProcess();

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
                    System.out.println("current time " + currentTime);
                    System.out.println(job.getJobNo() + " arrived");
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
