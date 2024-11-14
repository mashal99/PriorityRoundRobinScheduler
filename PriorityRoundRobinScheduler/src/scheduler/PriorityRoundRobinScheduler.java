package scheduler;

import java.util.LinkedList;
import java.util.Queue;

public class PriorityRoundRobinScheduler {
    private Queue<Process> highPriorityQueue = new LinkedList<>();
    private Queue<Process> mediumPriorityQueue = new LinkedList<>();
    private Queue<Process> lowPriorityQueue = new LinkedList<>();
    private static final int TIME_QUANTUM = 4;

    public void addProcess(Process process) {
        switch (process.priority) {
            case 1 -> highPriorityQueue.add(process);
            case 2 -> mediumPriorityQueue.add(process);
            case 3 -> lowPriorityQueue.add(process);
        }
    }

    public void schedule() {
        int currentTime = 0;
        int signalTime = 3;
        System.out.println("Gantt Chart:");

        while (!highPriorityQueue.isEmpty() || !mediumPriorityQueue.isEmpty() || !lowPriorityQueue.isEmpty()) {
            Process currentProcess = getNextProcess();
            int timeSlice = Math.min(TIME_QUANTUM, currentProcess.ticksRequired - currentProcess.ticksElapsed);

            for (int i = 0; i < timeSlice; i++) {
                currentTime++;
                currentProcess.incrementTicks();

                // Check for signal arrival
                if (currentTime == signalTime) {
                    currentProcess.receiveSignal();
                    signalTime += 3;
                }

                // Check for forking condition every 3 ticks
                if (currentProcess.shouldFork() && !currentProcess.isComplete()) {
                    Process forkedProcess = currentProcess.forkProcess();
                    if (forkedProcess != null) {
                        addProcess(forkedProcess);
                    }
                }
            }

            System.out.printf("[%s] from t=%d to t=%d\n", currentProcess.name, currentTime - timeSlice, currentTime);

            // If process is complete, do not add back to queue
            if (!currentProcess.isComplete()) {
                addProcess(currentProcess);
            }
        }

        displaySignals();
    }

    private Process getNextProcess() {
        if (!highPriorityQueue.isEmpty()) return highPriorityQueue.poll();
        if (!mediumPriorityQueue.isEmpty()) return mediumPriorityQueue.poll();
        return lowPriorityQueue.poll();
    }

    private void displaySignals() {
        System.out.println("\nSignals received by each process:");
        // Display the signal count of all processes
        // Note: This requires tracking all process instances globally or in a dedicated list
    }

    public static void main(String[] args) {
        PriorityRoundRobinScheduler scheduler = new PriorityRoundRobinScheduler();

        // Initialize processes
        scheduler.addProcess(new Process("P_A", 2, 10));
        scheduler.addProcess(new Process("P_B", 3, 7));

        // Run the scheduler
        scheduler.schedule();
    }
}
