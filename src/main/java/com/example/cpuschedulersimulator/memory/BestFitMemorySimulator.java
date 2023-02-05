package com.example.cpuschedulersimulator.memory;

import javafx.collections.ObservableList;


public class BestFitMemorySimulator extends MemorySimulator {


    public BestFitMemorySimulator(ObservableList<Process> processList, int memorySize, int osSize) {
        super(processList, memorySize, osSize);
    }

    public int getNextSlot(int slotSize) {

        int best_start = -1;
        int best_size = Integer.MAX_VALUE;
        int current_start = -1;
        int current_size = 0;

        for (int i = 0; i < main_memory.length; i++) {
            if (main_memory[i] == FREE_MEMORY) {
                if (current_size == 0) {
                    current_start = i;
                }
                current_size++;
            } else {
                if (current_size > 0) {
                    if (current_size < best_size && current_size >= slotSize) {
                        best_start = current_start;
                        best_size = current_size;
                    }
                    current_size = 0;
                }
            }
        }

        if (current_size < best_size && current_size >= slotSize) {
            best_start = current_start;
            best_size = current_size;
            current_size = 0;
        }

        return best_start;
    }
}
