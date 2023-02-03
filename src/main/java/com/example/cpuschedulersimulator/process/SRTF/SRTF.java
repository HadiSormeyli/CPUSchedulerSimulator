package com.example.cpuschedulersimulator.process.SRTF;

import com.example.cpuschedulersimulator.process.Algorithm;
import com.example.cpuschedulersimulator.process.CurrentProcess;
import com.example.cpuschedulersimulator.process.Job;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class SRTF extends Algorithm {

    public SRTF(ObservableList<Job> jobList) {
        super(jobList);
    }

    @Override
    public CurrentProcess nextStep(int currentTime) {
        Job currentJob = null;
        //boolean newJob = true;

        /**/
        addArrivedToTempQ(currentTime);

        sortByRemainingTime(readyQueue);


        /**/
        if (tempQueue.isEmpty() && readyQueue.isEmpty()) {
            currentJob = new Job(0, 0, 0); //all jobs finished
            currentProcess.setCurrentJob(currentJob);
        } else if (!tempQueue.isEmpty() && readyQueue.isEmpty()) {
            currentJob = new Job(-1, 0, 0); //waiting
            currentProcess.setCurrentJob(currentJob);

        } else if (!readyQueue.isEmpty()) {

            currentJob = readyQueue.get(0);
            readyQueue.remove(currentJob);

            readyQueue.forEach(job -> {
                job.incrementWaitTime();
            });

            if (!currentJob.isStarted()) {
                currentJob.setStartTime(currentTime);
                currentJob.setStarted(true);
            }

            currentJob.setRemainingTime(currentJob.remainingTime - 1);

            if (currentJob.remainingTime > 0) {
                //System.out.println(currentJob.getJobNo());
                readyQueue.add(currentJob);
                sortByRemainingTime(readyQueue);

            } else if (currentJob.remainingTime == 0) {
                //System.out.println("removed " + currentJob.getJobNo());
                readyQueue.remove(currentJob);
                currentJob.setFinishedTime(currentTime + 1);

                currentJob.setTurnAroundTime();
                currentProcess.tableData.set(currentJob.getJobNo() - 1, currentJob);
                sortByRemainingTime(readyQueue);
            }

            currentProcess.setCurrentJob(currentJob);
            currentProcess.tableData.set(currentJob.getJobNo() - 1, currentJob);
        }
        /**/

        return currentProcess;
    }
}

