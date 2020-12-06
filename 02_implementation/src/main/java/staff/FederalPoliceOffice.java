package staff;

import com.sun.jdi.PathSearchingVirtualMachine;
import explosivedevicecomponents.DisarmRobot;
import passenger.Passenger;
import test.write;

import java.util.ArrayList;
import java.util.List;

public class FederalPoliceOffice {
    private List<FederalPoliceOfficer> officerList = new ArrayList<>();
    private List<DisarmRobot> disarmRobots = new ArrayList<>();
    private List<Passenger> arrestedPassengers = new ArrayList<>();
    private boolean testFlag = false;

    public FederalPoliceOffice() {
        for (int i = 0; i < 3; i++) {
            this.disarmRobots.add(new DisarmRobot());
        }
    }

    public void registerNewOfficer(FederalPoliceOfficer officer) {
        this.officerList.add(officer);
    }

    public FederalPoliceOfficer[] requestReinforcement() {
        if (testFlag) new write().writeTestFile("requestInforcement");
        return officerList.subList(1,3).toArray(new FederalPoliceOfficer[2]);
    }

    public DisarmRobot getDisarmRobot() {
        return disarmRobots.get((int) System.currentTimeMillis() % disarmRobots.size());
    }

    public void takeArrestedPassengers(Passenger passenger) {
        if (!passenger.isArrested()) {
            arrestedPassengers.add(passenger);
            passenger.setArrested(true);

            System.out.println("Federal police office: Passenger was arrested");
        }
        this.arrestedPassengers.add(passenger);
    }

    public List<FederalPoliceOfficer> getAllOfficers() {
        return this.officerList;
    }

    public void setTestFlag(boolean testFlag) {
        this.testFlag = testFlag;
    }
}
