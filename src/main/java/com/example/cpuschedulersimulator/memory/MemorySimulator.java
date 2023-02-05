package com.example.cpuschedulersimulator.memory;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MemorySimulator {

    protected static final char FREE_MEMORY = '.';
    protected static final char RESERVED_MEMORY = '#';
    public static String OUT_OF_MEMORY = "OUT OF MEMORY... exiting!";
    private final int osSize;
    private final int blocksSize;
    protected int CURRENT_TIME = -1;
    protected char[] main_memory;
    protected ObservableList<Process> processes;
    protected String eventText = "";


    public MemorySimulator(ObservableList<Process> processList, int memorySize, int osSize) {
        main_memory = new char[memorySize];

        this.osSize = osSize;
        this.blocksSize = memorySize;
        processes = processList;
        initializeMainMemory();
    }

    protected abstract int getNextSlot(int slotSize);

    public String getEventText() {
        return eventText;
    }

    public ObservableList<Process> nextStep() {
        if (!eventText.equals(OUT_OF_MEMORY)) {
            CURRENT_TIME++;
            while (eventOccursAt(CURRENT_TIME)) {
                CURRENT_TIME++;
            }

            ArrayList<Process> toRemove = new ArrayList<Process>();
            for (Process p : processes) {
                if (p.getEndTime() == CURRENT_TIME) {
                    removeFromMemory(p);
                    toRemove.add(p);
                }
            }
            for (Process p : toRemove) {
                processes.remove(p);
            }

            for (Process p : processes) {
                if (p.getStartTime() == CURRENT_TIME) {
                    putInMemory(p);
                }
            }
            printMemory();
            return processes;
        }
        return null;
    }

    public ObservableList<Process> getFreeSpace() {
        if (!eventText.equals(OUT_OF_MEMORY)) {
            List<Process> list;
            ObservableList<Process> freeSpaces = FXCollections.observableArrayList();

            list = processes.stream().filter(process -> process.getStatus() == Process.Status.ALLOCATED)
                    .sorted(Comparator.comparing(Process::getLocation))
                    .collect(Collectors.toList());


            int location = osSize;
            int free;

            for (Process p : list) {
                free = p.getLocation() - location;
                if (free > 0) {
                    freeSpaces.add(new Process(freeSpaces.size(), free, location, Process.Status.AVAILABLE));
                }
                location = p.getLocation() + p.getSize();
            }

            free = main_memory.length - location;
            if (free > 0) {
                freeSpaces.add(new Process(freeSpaces.size(), free, location, Process.Status.AVAILABLE));
            }
            return freeSpaces;
        }
        return null;
    }

    public void timeStepUntil(int t) {
        while (CURRENT_TIME < t) {
            CURRENT_TIME++;
            while (eventOccursAt(CURRENT_TIME) && CURRENT_TIME < t) {
                CURRENT_TIME++;
            }

            ArrayList<Process> toRemove = new ArrayList<Process>();
            for (Process p : processes) {
                if (p.getEndTime() == CURRENT_TIME) {
                    removeFromMemory(p);
                    toRemove.add(p);
                }
            }
            for (Process p : toRemove) {
                processes.remove(p);
            }

            for (Process p : processes) {
                if (p.getStartTime() == CURRENT_TIME) {
                    putInMemory(p);
                }
            }
        }
    }

    private boolean eventOccursAt(int time) {
        for (Process p : processes) {
            if (p.getStartTime() == time) {
                eventText = "P" + p.getNumber() + " START AT " + CURRENT_TIME;
                return false;
            } else if (p.getEndTime() == time) {
                eventText = "P" + p.getNumber() + " END AT " + CURRENT_TIME;
                return false;
            }
        }
        return true;
    }

    protected void putInMemory(Process p) {
        int targetSlot = getNextSlot(p.getSize());
        if (targetSlot == -1) {
            deFragment();
            targetSlot = getNextSlot(p.getSize());
            if (targetSlot == -1) {
                eventText = OUT_OF_MEMORY;
                return;
            }
        }
        for (int i = 0; i < p.getSize(); i++) {
            main_memory[i + targetSlot] = (char) (p.getNumber() + '0');
        }
        p.setLocation(targetSlot);
        p.setStatus(Process.Status.ALLOCATED);
    }

    protected void removeFromMemory(Process p) {
        for (int i = 0; i < main_memory.length; i++) {
            if (main_memory[i] == (char) (p.getNumber() + '0')) {
                main_memory[i] = FREE_MEMORY;
            }
        }
        p.setLocation(-1);
        p.setStatus(Process.Status.DONE);
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

    private void deFragment() {
        HashMap<Character, Integer> processesMoved = new HashMap<Character, Integer>();
        DecimalFormat f = new DecimalFormat("##.00");

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

    public int getOsSize() {
        return osSize;
    }

    public int processesRemaining() {
        return processes.size();
    }

    public int getMemorySize() {
        return main_memory.length;
    }
}
