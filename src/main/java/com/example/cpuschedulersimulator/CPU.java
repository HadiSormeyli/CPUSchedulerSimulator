package com.example.cpuschedulersimulator;

import com.example.cpuschedulersimulator.process.Algorithm;
import com.example.cpuschedulersimulator.process.CurrentProcess;
import com.example.cpuschedulersimulator.process.FCFS.FCFS;
import com.example.cpuschedulersimulator.process.Job;
import com.example.cpuschedulersimulator.process.RR.RR;
import com.example.cpuschedulersimulator.process.SJF.SJF;
import com.example.cpuschedulersimulator.process.SRTF.SRTF;
import javafx.collections.ObservableList;

public class CPU {

    private static Algorithm algorithm;
    //public static int currentTime;
    public static int quantum;
    public static ObservableList<Job> jobList;

    public static CurrentProcess nextStep(int currentTime) {
        CurrentProcess currentProcess = algorithm.nextStep(currentTime);
//        System.out.println(job.toString());
        return currentProcess;
    }

    public static void setJobList(ObservableList<Job> jobList) {
        CPU.jobList = jobList;
    }

    public static void setQuantum(int quantum) {
        CPU.quantum = quantum;
    }

    public static int getQuantum() {
        return quantum;
    }


    public static void setAlgorithm(int algorithm) {
        switch (algorithm) {
            case 0 -> CPU.algorithm = new FCFS(jobList);
            case 1 -> CPU.algorithm = new SJF(jobList);
            case 2 -> CPU.algorithm = new SRTF(jobList);
            case 3 -> CPU.algorithm = new RR(jobList);
        }
    }
}