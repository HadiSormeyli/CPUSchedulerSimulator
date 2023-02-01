package com.example.cpuschedulersimulator.process;


import java.util.ArrayList;

import static com.example.cpuschedulersimulator.process.Job.*;


public abstract class Algorithm {
    ArrayList<Job> jobList, tempQueue, currentProcessData;
    ArrayList<Job> readyQueue = new ArrayList<>();
    Job currentJob;
    CurrentProcess currentProcess = new CurrentProcess();

    public Algorithm(ArrayList<Job> jobList) {
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

    public ArrayList<Job> sortByArrivalTime(ArrayList<Job> list) {
        list.sort(arrivalTimeComparator);
        return list;
    }

    public ArrayList<Job> sortByBurstTime(ArrayList<Job> list) {
        list.sort(burstTimeComparator);
        return list;
    }

    public ArrayList<Job> sortByRemainingTime(ArrayList<Job> list) {
        list.sort(remainingTimeComparator);
        return list;
    }

    public ArrayList<Job> arrayListCopy(ArrayList<Job> list) {
        ArrayList<Job> copy = new ArrayList<>();

        list.forEach(job -> {
            copy.add(job.getCopy());
        });

        return copy;
    }
}
