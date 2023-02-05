package com.example.cpuschedulersimulator.memory;

import javafx.collections.ObservableList;


public class WorstFitMemorySimulator extends MemorySimulator {


    public WorstFitMemorySimulator(ObservableList<Process> processList, int memorySize, int osSize) {
        super(processList, memorySize, osSize);
    }

    @Override
    protected int getNextSlot(int slotSize) {
        int best_start = -1;
        int current_start = -1;
        int biggest_size = 0;
        int found_size = 0;

        for (int i = 0; i < main_memory.length; i++) {
            if (main_memory[i] == FREE_MEMORY) {
                if (found_size == 0) {
                    current_start = i;
                }
                found_size++;
            } else {
                if (found_size > biggest_size) {
                    biggest_size = found_size;
                    best_start = current_start;
                }
                found_size = 0;
            }
        }

        if (found_size > biggest_size) {
            biggest_size = found_size;
            best_start = current_start;
        }

        if (slotSize > biggest_size) {
            return -1;
        } else {
            return best_start;
        }
    }
}
