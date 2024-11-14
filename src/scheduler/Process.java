package scheduler;

public class Process {
    String name;
    int priority;
    int ticksRequired;
    int ticksElapsed = 0;
    int signalsReceived = 0;

    public Process(String name, int priority, int ticksRequired) {
        this.name = name;
        this.priority = priority;
        this.ticksRequired = ticksRequired;
    }

    public boolean isComplete() {
        return ticksElapsed >= ticksRequired;
    }

    public void receiveSignal() {
        signalsReceived++;
    }

    public void incrementTicks() {
        ticksElapsed++;
    }

    public boolean shouldFork() {
        return ticksElapsed % 3 == 0 && ticksElapsed > 0;
    }

    public Process forkProcess() {
        if (name.equals("P_A")) {
            return new Process("P_B", 3, 7);
        } else if (name.equals("P_B")) {
            return new Process("P_C", 1, 5);
        }
        return null;
    }
}
