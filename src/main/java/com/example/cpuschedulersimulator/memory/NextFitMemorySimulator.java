package com.example.cpuschedulersimulator.memory;

import javafx.collections.ObservableList;


public class NextFitMemorySimulator extends MemorySimulator {


    public NextFitMemorySimulator(ObservableList<Process> processList, int memorySize, int osSize) {
        super(processList, memorySize, osSize);
    }

    @Override
    protected int getNextSlot(int slotSize) {
        int spaceAvailable = 0;
        for (int i = main_memory.length - 1; i >= 0; i--) {
            if (main_memory[i] == FREE_MEMORY) {
                spaceAvailable++;
            } else {
                if (spaceAvailable < slotSize) {
                    return -1;
                } else {
                    return i + 1;
                }
            }
        }
        return -1;
    }
}
