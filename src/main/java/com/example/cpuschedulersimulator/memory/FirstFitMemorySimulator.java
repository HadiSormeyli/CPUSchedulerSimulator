package com.example.cpuschedulersimulator.memory;


import javafx.collections.ObservableList;

public class FirstFitMemorySimulator extends MemorySimulator {

    public FirstFitMemorySimulator(ObservableList<Process> processList, int memorySize, int osSize) {
        super(processList, memorySize, osSize);
    }

    @Override
    public int getNextSlot(int slotSize) {
        int blocksize = 0;

        int i;
        for (i = 0; i < main_memory.length - slotSize; i++) {
            if (main_memory[i] == FREE_MEMORY && blocksize < slotSize)
                blocksize++;
            else {
                if (blocksize >= slotSize)
                    return i - blocksize;
                blocksize = 0;
            }
        }

        return -1;
    }
}
