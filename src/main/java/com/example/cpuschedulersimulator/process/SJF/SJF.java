package com.example.cpuschedulersimulator.process.SJF;

import com.example.cpuschedulersimulator.process.Algorithm;
import com.example.cpuschedulersimulator.process.CurrentProcess;
import com.example.cpuschedulersimulator.process.Job;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class SJF extends Algorithm {

    boolean newJob = true;

    public SJF(ObservableList<Job> jobList) {
        super(jobList);
    }

    @Override
    public CurrentProcess nextStep(int currentTime) {
        Job currentJob = null;
        //System.out.println("table data 1 "+currentProcess.tableData.toString());

        /**/
        addArrivedToTempQ(currentTime);

        if (newJob) {
            sortByBurstTime(readyQueue);
            //System.out.println(readyQueue.toString());
        }

        /**/
        if (tempQueue.isEmpty() && readyQueue.isEmpty()) {
            currentJob = new Job(0, 0, 0); //all jobs finished
            currentProcess.setCurrentJob(currentJob);
        } else if (!tempQueue.isEmpty() && readyQueue.isEmpty()) {
            currentJob = new Job(-1, 0, 0); //waiting
            currentProcess.setCurrentJob(currentJob);

        } else if (!readyQueue.isEmpty()) {

            currentJob = readyQueue.get(0);
            newJob = false;

            if (!currentJob.isStarted()) {
                currentJob.setStartTime(currentTime);
                currentJob.setStarted(true);
            }
            currentJob.setRemainingTime(currentJob.remainingTime - 1);

            if (currentJob.remainingTime > 0) {
                //System.out.println(currentJob.getJobNo());
                readyQueue.set(0, currentJob);
                currentProcess.tableData.set(currentJob.getJobNo() - 1, currentJob);

            } else if (currentJob.remainingTime == 0) {
                //System.out.println("removed " + currentJob.getJobNo());
                readyQueue.remove(currentJob);
                currentJob.setFinishedTime(currentTime + 1);
                currentJob.setWaitTime();
                currentJob.setTurnAroundTime();
                currentProcess.tableData.set(currentJob.getJobNo() - 1, currentJob);
                newJob = true;
            }

            currentProcess.setCurrentJob(currentJob);

            //System.out.println("table data last " +currentProcess.tableData.toString());
        }
        /**/

        return currentProcess;
    }
}
