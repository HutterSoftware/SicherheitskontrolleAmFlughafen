package components;

import State.Shutdown;
import staff.Employee;
import staff.Supervisor;

public class Supervision {

    private BaggageScanner scanner;
    private Employee employee;

    public Supervision(BaggageScanner scanner) {
        this.scanner = scanner;
    }

    public BaggageScanner getScanner() {
        return scanner;
    }

    public void setScanner(BaggageScanner scanner) {
        this.scanner = scanner;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void pressPowerButton() {
        if (scanner.getCurrentState() instanceof Shutdown) {
            scanner.setCurrentState(scanner.getCurrentState().start());
            System.out.println("Supervision: Power button pressed");
        } else {
            scanner.setCurrentState(scanner.getCurrentState().shutdown());
        }
    }
}
