package com.example.cpuschedulersimulator.process;

import java.util.ArrayList;

public class CurrentProcess {
    public ArrayList<Job> tableData = new ArrayList<Job>();
    public Job currentJob;

    public ArrayList<Job> getTableData() {
        return tableData;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setTableData(ArrayList<Job> tableData) {
        this.tableData = tableData;
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }
}
