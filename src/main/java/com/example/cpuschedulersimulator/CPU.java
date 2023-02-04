package com.example.cpuschedulersimulator;

import com.example.cpuschedulersimulator.memory.Process;
import com.example.cpuschedulersimulator.memory.*;
import com.example.cpuschedulersimulator.process.Algorithm;
import com.example.cpuschedulersimulator.process.CurrentProcess;
import com.example.cpuschedulersimulator.process.FCFS.FCFS;
import com.example.cpuschedulersimulator.process.Job;
import com.example.cpuschedulersimulator.process.RR.RR;
import com.example.cpuschedulersimulator.process.SJF.SJF;
import com.example.cpuschedulersimulator.process.SRTF.SRTF;
import javafx.collections.ObservableList;

public class CPU {

    public static MemorySimulatorBase memorySimulatorBase;
    public static ObservableList<Process> processList;

    private static Algorithm algorithm;
    public static ObservableList<Job> jobList;
    public static int quantum;

    public static ObservableList<Process> nextMemoryStep() {
        return memorySimulatorBase.nextStep();
    }

    public static void setProcessList(ObservableList<Process> processList) {
        CPU.processList = processList;
    }

    public static void setMemoryAlgorithm(int algorithm, int memorySize, int osSize, int blocksSize) {
        switch (algorithm) {
            case 0 -> CPU.memorySimulatorBase = new FirstFitMemorySimulator(processList, memorySize, osSize, blocksSize);
            case 1 -> CPU.memorySimulatorBase = new BestFitMemorySimulator(processList, memorySize, osSize, blocksSize);
            case 2 -> CPU.memorySimulatorBase = new WorstFitMemorySimulator(processList, memorySize, osSize, blocksSize);
        }
    }

    public static CurrentProcess nextProcessStep(int currentTime) {
        return algorithm.nextStep(currentTime);
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

    public static void setProcessAlgorithm(int algorithm) {
        switch (algorithm) {
            case 0 -> CPU.algorithm = new FCFS(jobList);
            case 1 -> CPU.algorithm = new SJF(jobList);
            case 2 -> CPU.algorithm = new SRTF(jobList);
            case 3 -> CPU.algorithm = new RR(jobList);
        }
    }
}