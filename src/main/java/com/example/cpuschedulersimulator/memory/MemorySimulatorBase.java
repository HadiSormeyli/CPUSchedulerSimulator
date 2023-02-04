package com.example.cpuschedulersimulator.memory;


import javafx.collections.ObservableList;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class MemorySimulatorBase {
    protected static final char FREE_MEMORY = '.';
    protected static final char RESERVED_MEMORY = '#';
    protected int CURRENT_TIME = -1;

    protected char[] main_memory;
    protected ObservableList<Process> processes;

    protected static final boolean MEMSIM_DEBUG = false;

    private final int osSize;

    private final int blocksSize;


    public MemorySimulatorBase(ObservableList<Process> processList, int memorySize, int osSize, int blocksSize) {
        main_memory = new char[memorySize];

        this.osSize = osSize;
        this.blocksSize = blocksSize;
        processes = processList;
        initializeMainMemory();
        for (Process p : processes) {
            debugPrintln("Process " + (char) (p.getNumber() + '0') + " (size " + p.getSize() + ")");
            debugPrintln("  Start Time: " + p.getStartTime());
            debugPrintln("  End Time: " + p.getEndTime());
        }
    }

    protected abstract int getNextSlot(int slotSize);

    public ObservableList<Process> nextStep() {
        CURRENT_TIME++;
        while (!eventOccursAt(CURRENT_TIME)) {
            debugPrintln("Fast-forwarding past boring time " + CURRENT_TIME);
            CURRENT_TIME++;
        }

        debugPrintln("=========== TIME IS NOW " + CURRENT_TIME + " ============");

        //Processes exit the system
        ArrayList<Process> toRemove = new ArrayList<Process>();
        for (Process p : processes) {
            if (p.getEndTime() == CURRENT_TIME) {
                debugPrintln("Removing process " + p.getNumber());
                removeFromMemory(p);
                toRemove.add(p);
            }
        }
        for (Process p : toRemove) {
            processes.remove(p);
        }

        //Processes enter the system
        for (Process p : processes) {
            if (p.getStartTime() == CURRENT_TIME) {
                debugPrintln("Adding process " + p.getNumber());
                putInMemory(p);
            }
        }
        printMemory();
        return processes;
    }

    public void timeStepUntil(int t) {
        while (CURRENT_TIME < t) {
            CURRENT_TIME++;
            while (!eventOccursAt(CURRENT_TIME) && CURRENT_TIME < t) {
                debugPrintln("Fast-forwarding past boring time " + CURRENT_TIME);
                CURRENT_TIME++;
            }

            debugPrintln("=========== TIME IS NOW " + CURRENT_TIME + " ============");

            //Processes exit the system
            ArrayList<Process> toRemove = new ArrayList<Process>();
            for (Process p : processes) {
                if (p.getEndTime() == CURRENT_TIME) {
                    debugPrintln("Removing process " + p.getNumber());
                    removeFromMemory(p);
                    toRemove.add(p);
                }
            }
            for (Process p : toRemove) {
                processes.remove(p);
            }

            //Processes enter the system
            for (Process p : processes) {
                if (p.getStartTime() == CURRENT_TIME) {
                    debugPrintln("Adding process " + p.getNumber());
                    putInMemory(p);
                }
            }
        }
    }

    private boolean eventOccursAt(int time) {
        for (Process p : processes) {
            if (p.getStartTime() == time || p.getEndTime() == time) {
                return true;
            }
        }
        return false;
    }

    protected void putInMemory(Process p) {
        int targetSlot = getNextSlot(p.getSize());
        if (targetSlot == -1) {
            defragment();
            targetSlot = getNextSlot(p.getSize());
            if (targetSlot == -1) {
                Externals.outOfMemoryExit();
            }
        }
        debugPrintln("Got a target slot of " + targetSlot + " for pid " + p.getNumber());
        //If we get here, we know that there's an open chunk
        for (int i = 0; i < p.getSize(); i++) {
            main_memory[i + targetSlot] = (char) (p.getNumber() + '0');
        }
    }

    protected void removeFromMemory(Process p) {
        for (int i = 0; i < main_memory.length; i++) {
            if (main_memory[i] == (char) (p.getNumber() + '0')) {
                main_memory[i] = FREE_MEMORY;
            }
        }
    }

    private void initializeMainMemory() {
        for (int i = 0; i < osSize && i < main_memory.length; i++) {
            main_memory[i] = RESERVED_MEMORY;
        }
        for (int i = osSize; i < main_memory.length; i++) {
            main_memory[i] = FREE_MEMORY;
        }
    }

    public void printMemory() {
        System.out.print("Memory at time " + CURRENT_TIME + ":");
        for (int i = 0; i < main_memory.length; i++) {
            if (i % blocksSize == 0) {
                System.out.println("");
            }
            System.out.print(main_memory[i] + "");
        }
        System.out.println("");
    }

    private void defragment() {
        HashMap<Character, Integer> processesMoved = new HashMap<Character, Integer>();
        DecimalFormat f = new DecimalFormat("##.00");

        System.out.println("Performing defragmentation...");

        int destination = blocksSize;
        for (int i = 0; i < main_memory.length; i++) {
            if (main_memory[i] != FREE_MEMORY
                    && main_memory[i] != RESERVED_MEMORY
                    && i != destination) {
                main_memory[destination] = main_memory[i];
                main_memory[i] = FREE_MEMORY;
                destination++;
                processesMoved.put(main_memory[i], null);
            }
        }
        int numMoved = processesMoved.size();
        int freeBlockSize = main_memory.length - destination;
        double percentage = (double) freeBlockSize / (double) main_memory.length;

        System.out.println("Defragmentation completed.");
        System.out.println("Relocated " + numMoved + " processes " +
                "to create a free memory block of " + freeBlockSize + " units " +
                "(" + f.format(percentage * 100) + "% of total memory).");
    }

    private static void debugPrint(String toPrint) {
        if (MEMSIM_DEBUG == true) {
            System.out.print(toPrint);
        }
    }

    private static void debugPrintln(String toPrint) {
        if (MEMSIM_DEBUG == true) {
            System.out.println(toPrint);
        }
    }

    public int processesRemaining() {
        return processes.size();
    }
}
