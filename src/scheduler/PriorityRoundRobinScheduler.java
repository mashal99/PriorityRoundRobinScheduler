package scheduler;

public class PriorityRoundRobinScheduler {
    private static final int TIME_QUANTUM = 4;
    private ProcessQueue processQueue = new ProcessQueue();
    private GanttChart ganttChart = new GanttChart();
    private SignalHandler signalHandler = new SignalHandler();

    public void addProcess(Process process) {
        processQueue.addProcess(process);
    }

    public void schedule() {
        int currentTime = 0;

        while (!processQueue.isEmpty()) {
            Process currentProcess = processQueue.getNextProcess();
            int timeSlice = Math.min(TIME_QUANTUM, currentProcess.ticksRequired - currentProcess.ticksElapsed);
            int startTime = currentTime;

            for (int i = 0; i < timeSlice; i++) {
                currentTime++;
                currentProcess.incrementTicks();

                // Check for signal
                signalHandler.checkAndHandleSignal(currentProcess, currentTime);

                // Check for forking
                if (currentProcess.shouldFork() && !currentProcess.isComplete()) {
                    Process forkedProcess = currentProcess.forkProcess();
                    if (forkedProcess != null) {
                        processQueue.addProcess(forkedProcess);
                    }
                }
            }

            ganttChart.addEntry(currentProcess, startTime, currentTime);

            if (!currentProcess.isComplete()) {
                processQueue.addProcess(currentProcess);
            }

            // Display the current state of the ready queue after each process execution slice
            processQueue.displayQueue();
        }

        ganttChart.display();
        displaySignals();
    }

    private void displaySignals() {
        System.out.println("\nSignals received by each process:");
        // Logic to display signal counts for each process
    }

    public static void main(String[] args) {
        PriorityRoundRobinScheduler scheduler = new PriorityRoundRobinScheduler();
        scheduler.addProcess(new Process("P_A", 2, 10));
        scheduler.addProcess(new Process("P_B", 3, 7));
        scheduler.schedule();
    }
}
