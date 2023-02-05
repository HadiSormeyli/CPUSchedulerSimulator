package com.example.cpuschedulersimulator.process.RR;

import com.example.cpuschedulersimulator.CPU;
import com.example.cpuschedulersimulator.process.Algorithm;
import com.example.cpuschedulersimulator.process.CurrentProcess;
import com.example.cpuschedulersimulator.process.Job;
import javafx.collections.ObservableList;

public class RR extends Algorithm {

    private int iterations = 0;

    public RR(ObservableList<Job> jobList) {
        super(jobList);
    }

    @Override
    public CurrentProcess nextStep(int currentTime) {
        Job currentJob = null;

        addArrivedToTempQ(currentTime);

        if (tempQueue.isEmpty() && readyQueue.isEmpty()) {
            currentJob = new Job(0, 0, 0);
            iterations = 0;
            currentProcess.setCurrentJob(currentJob);
        } else if (!tempQueue.isEmpty() && readyQueue.isEmpty()) {
            currentJob = new Job(-1, 0, 0);
            iterations = 0;
            currentProcess.setCurrentJob(currentJob);
        } else if (!readyQueue.isEmpty()) {
            currentJob = readyQueue.get(0);
            readyQueue.remove(currentJob);
            iterations++;

            readyQueue.forEach(Job::incrementWaitTime);

            if (!currentJob.isStarted()) {
                currentJob.setStartTime(currentTime);
                currentJob.setStarted(true);
            }

            currentJob.setRemainingTime(currentJob.remainingTime - 1);

            if (currentJob.remainingTime > 0 && iterations < CPU.getQuantum()) {
                addArrivedToTempQ(currentTime);
                readyQueue.add(0, currentJob);
            } else if (currentJob.remainingTime > 0 && iterations >= CPU.getQuantum()) {
                addArrivedToTempQ(currentTime + 1);
                readyQueue.add(currentJob);
                iterations = 0;
            } else if (currentJob.remainingTime == 0) {
                readyQueue.remove(currentJob);
                currentJob.setFinishedTime(currentTime + 1);
                iterations = 0;
                currentJob.setTurnAroundTime();
                currentProcess.tableData.set(currentJob.getJobNo() - 1, currentJob);
            }

            currentProcess.setCurrentJob(currentJob);
            currentProcess.tableData.set(currentJob.getJobNo() - 1, currentJob);
        }

        return currentProcess;
    }
}
