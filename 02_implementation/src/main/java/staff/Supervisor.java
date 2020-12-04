package staff;

import components.BaggageScanner;
import components.IBaggageScanner;

public class Supervisor extends Employee {
    private boolean isSenior;
    private boolean isExecutive;

    public Supervisor(String id, String name, String birthDate, boolean isSenior, boolean isExecutive) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public void unlockBaggageScanner(BaggageScanner baggageScanner)  {
        System.out.println("Supervisor: Unlock baggage scanner");
        enterPin(baggageScanner.getOperationStation().getReader());
    }

    public void switchPower(BaggageScanner baggageScanner) {
        System.out.println("Supervisor: Turn baggage scanner on");
        baggageScanner.getSupervision().pressPowerButton();
    }

    public boolean isSenior() {
        return isSenior;
    }

    public boolean isExecutive() {
        return isExecutive;
    }
}
