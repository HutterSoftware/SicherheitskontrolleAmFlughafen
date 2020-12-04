package staff;

import components.BaggageScanner;
import explosivedevicecomponents.DisarmRobot;
import passenger.Passenger;

public class FederalPoliceOfficer extends Employee {
    private String grade;
    private FederalPoliceOffice office;

    public FederalPoliceOfficer(String id, String name, String birthDate, String grade) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.grade = grade;
        this.office = office;
    }

    public String getGrade() {
        return this.grade;
    }

    public FederalPoliceOffice getOffice() {
        return this.office;
    }

    public void setOffice(FederalPoliceOffice office) {
        this.office = office;
    }

    public void arrestPassenger(Passenger passenger) {

    }

    public void takeWeapon(String weapon) {
        System.out.println("Federal police officer: Weapon was confiscated");
    }

    public void steerRobot(DisarmRobot robot, BaggageScanner baggageScanner) {
        robot.destroyBaggage(baggageScanner.getManualPostControl().getCurrentTrayToInvestigate().takeBaggage());
    }
}
