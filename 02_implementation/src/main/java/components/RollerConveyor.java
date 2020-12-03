package components;

import staff.Employee;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class RollerConveyor {

    private BaggageScanner scanner;
    private Deque<Tray> trays = new LinkedBlockingDeque<>();
    private Employee workingInspector;

    public RollerConveyor(BaggageScanner scanner) {
        this.scanner = scanner;
    }

    public void addTray(Tray tray) {
        this.trays.addLast(tray);
    }

    public void pushTrays() {
        scanner.getBelt().queueTray(trays.pollFirst());
    }

    public Employee getWorkingInspector() {
        return workingInspector;
    }

    public void setWorkingInspector(Employee workingInspector) {
        this.workingInspector = workingInspector;
    }

    public Deque<Tray> getTrayQueue() {
        return this.trays;
    }
}
