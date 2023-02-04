package com.example.cpuschedulersimulator.memory;

import javafx.collections.ObservableList;

/**
 * Memory strategy that puts a process in memory noncontinguously.
 */
public class NonContiguousMemorySimulator extends MemorySimulatorBase {


    public NonContiguousMemorySimulator(ObservableList<Process> processList, int memorySize, int osSize, int blocksSize) {
        super(processList, memorySize, osSize, blocksSize);
    }

    /**
     * Return the index of the first position of the next available slot
     * in memory
     *
     * @param slotSize The size of the requested slot
     * @return The index of the first position of an available requested block
     */
    @Override
    protected int getNextSlot(int slotSize) {
        throw new IllegalArgumentException("This method isn't relevant " +
                "for this memory allocation strategy");
    }

    /**
     * Put a process in memory (noncontiguously).
     *
     * @param p The process to put in memory.
     */
    @Override
    protected void putInMemory(Process p) {
        int remainingToPlace = p.getSize();
        for (int i = 0; i < main_memory.length && remainingToPlace > 0; i++) {
            if (main_memory[i] == FREE_MEMORY) {
                main_memory[i] = (char) p.getNumber();
                remainingToPlace--;
            }
        }
        if (remainingToPlace > 0) {
            Externals.outOfMemoryExit();
        }
    }

}
