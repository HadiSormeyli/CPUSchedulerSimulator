package com.example.cpuschedulersimulator.process;

import javafx.scene.paint.Color;

import java.util.Comparator;

public class Job {

    public static Comparator<Job> arrivalTimeComparator = (j1, j2) -> {
        int arrivalTime1 = j1.getArrivalTime();
        int arrivalTime2 = j2.getArrivalTime();
        return arrivalTime1 - arrivalTime2;
    };
    public static Comparator<Job> burstTimeComparator = (j1, j2) -> {
        int burstTime1 = j1.getBurstTime();
        int burstTime2 = j2.getBurstTime();

        return burstTime1 - burstTime2;
    };
    public static Comparator<Job> remainingTimeComparator = (j1, j2) -> {
        int remainingTime1 = j1.getRemainingTime();
        int remainingTime2 = j2.getRemainingTime();

        return remainingTime1 - remainingTime2;
    };
    private final Color color = Color.color(Math.random(), Math.random(), Math.random());
    public int jobNo;
    public int arrivalTime;
    public int burstTime;
    public int startTime;
    public int waitTime;
    public int remainingTime;
    public int finishedTime;
    public boolean finished;
    public boolean started;
    public int turnAroundTime;

    public Job(int jobNo) {
        this.jobNo = jobNo;
    }

    public Job(int jobNo, int arrivalTime, int burstTime) {
        this.jobNo = jobNo;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;

        this.startTime = 0;
        this.finishedTime = 0;
        this.finished = false;
        this.started = false;
        this.remainingTime = burstTime;
        this.waitTime = 0;
        this.turnAroundTime = 0;
    }

    public Color getColor() {
        return color;
    }

    public void setWaitTime() {
        this.waitTime = startTime - arrivalTime;
    }

    public void incrementWaitTime() {
        this.waitTime = this.waitTime + 1;
    }

    public void setTurnAroundTime() {
        this.turnAroundTime = waitTime + burstTime;
    }

    public int getJobNo() {
        return jobNo;
    }

    public void setJobNo(int jobNo) {
        this.jobNo = jobNo;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(int finishedTime) {
        this.finishedTime = finishedTime;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public Job getCopy() {
        Job copy = new Job(this.jobNo, this.arrivalTime, this.burstTime);
        return copy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.jobNo;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Job other = (Job) obj;
        if (this.jobNo != other.jobNo) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "[ jobNo=" + jobNo + ", arrivalTime=" + arrivalTime + ", burstTime=" + burstTime + ", remainingTime =" + remainingTime + ", startTime=" + startTime + ", "
                + "waitTime=" + waitTime + ", finishedTime=" + finishedTime + ", finished=" + finished + ", turnAroundTime=" + turnAroundTime + "] \n";
    }
}
