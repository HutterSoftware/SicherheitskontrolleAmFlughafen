package staff;

import components.IBaggageScanner;

public class Technician extends Employee {
    public Technician(String id, String name, String birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public void executeMaintenance(IBaggageScanner baggageScanner) {
        baggageScanner.maintenance();
        System.out.println("Technician: Perform maintenance");
    }
}
