package com.example.cpuschedulersimulator.memory;


public class Process implements Comparable<Process> {
    private int number;
    private int size;
    private int start_time;
    private int end_time;

    public Process(int number, int size, int start_time, int end_time) {
        this.number = number;
        this.size = size;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Process(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public int getStartTime() {
        return start_time;
    }

    public int getEndTime() {
        return end_time;
    }

    @Override
    public int compareTo(Process o) {
        return Integer.compare(this.start_time, o.start_time);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }
}
