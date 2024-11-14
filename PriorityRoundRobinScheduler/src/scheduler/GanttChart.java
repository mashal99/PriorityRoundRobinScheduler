package scheduler;

public class GanttChart {
    private StringBuilder chartContent = new StringBuilder("Gantt Chart:\n");

    public void addEntry(Process process, int startTime, int endTime) {
        chartContent.append(String.format("[%s] from t=%d to t=%d\n", process.name, startTime, endTime));
    }

    public void display() {
        System.out.println(chartContent.toString());
    }
}
