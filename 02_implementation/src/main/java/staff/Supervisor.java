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

    }

    public void switchPower(BaggageScanner baggageScanner) {

    }

    public boolean isSenior() {
        return isSenior;
    }

    public boolean isExecutive() {
        return isExecutive;
    }
}
