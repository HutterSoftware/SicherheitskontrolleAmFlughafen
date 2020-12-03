package staff;

import com.sun.jdi.PathSearchingVirtualMachine;
import explosivedevicecomponents.DisarmRobot;
import passenger.Passenger;

import java.util.ArrayList;
import java.util.List;

public class FederalPoliceOffice {
    private List<FederalPoliceOfficer> officerList = new ArrayList<>();
    private List<DisarmRobot> disarmRobots = new ArrayList<>();
    private List<Passenger> arrestedPassengers = new ArrayList<>();

    public FederalPoliceOffice() {
        for (int i = 0; i < 3; i++) {
            this.disarmRobots.add(new DisarmRobot());
        }
    }

    public void registerNewOfficer(FederalPoliceOfficer officer) {
        this.officerList.add(officer);
    }

    public FederalPoliceOfficer[] requestReinforcement() {
        return officerList.subList(1,3).toArray(new FederalPoliceOfficer[2]);
    }

    public DisarmRobot getDisarmRobot() {
        return disarmRobots.get((int) System.currentTimeMillis() % disarmRobots.size());
    }

    public void takeArrestedPassengers(Passenger passenger) {
        this.arrestedPassengers.add(passenger);
    }

    public List<FederalPoliceOfficer> getAllOfficers() {
        return this.officerList;
    }
}
