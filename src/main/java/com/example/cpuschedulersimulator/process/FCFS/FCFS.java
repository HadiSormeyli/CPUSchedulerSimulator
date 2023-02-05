package com.example.cpuschedulersimulator.process.FCFS;


import com.example.cpuschedulersimulator.process.Algorithm;
import com.example.cpuschedulersimulator.process.CurrentProcess;
import com.example.cpuschedulersimulator.process.Job;
import javafx.collections.ObservableList;


public class FCFS extends Algorithm {


    public FCFS(ObservableList<Job> jobList) {
        super(jobList);
    }

    @Override
    public CurrentProcess nextStep(int currentTime) {
        Job currentJob = null;

        addArrivedToTempQ(currentTime);

        if (tempQueue.isEmpty() && readyQueue.isEmpty()) {
            currentJob = new Job(0, 0, 0);
            currentProcess.setCurrentJob(currentJob);
        } else if (!tempQueue.isEmpty() && readyQueue.isEmpty()) {
            currentJob = new Job(-1, 0, 0);
            currentProcess.setCurrentJob(currentJob);
        } else if (!readyQueue.isEmpty()) {
            currentJob = readyQueue.get(0);
            if (!currentJob.isStarted()) {
                currentJob.setStartTime(currentTime);
                currentJob.setStarted(true);
            }
            currentJob.setRemainingTime(currentJob.remainingTime - 1);

            if (currentJob.remainingTime > 0) {
                readyQueue.set(0, currentJob);
            } else if (currentJob.remainingTime == 0) {
                readyQueue.remove(currentJob);
                currentJob.setFinishedTime(currentTime + 1);
                currentJob.setWaitTime();
                currentJob.setTurnAroundTime();
                currentProcess.tableData.set(currentJob.getJobNo() - 1, currentJob);
            }

            currentProcess.setCurrentJob(currentJob);
            currentProcess.tableData.set(currentJob.getJobNo() - 1, currentJob);
        }
        return currentProcess;
    }
}
