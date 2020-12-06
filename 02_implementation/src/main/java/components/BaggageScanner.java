package components;

import State.State;
import data.Record;
import passenger.Passenger;
import staff.FederalPoliceOfficer;
import test.write;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BaggageScanner implements  IBaggageScanner{

    private Tray tray;
    private Scanner scanner;
    private RollerConveyor rollerConveyor;
    private Belt belt;
    private OperationStation operationStation;
    private ManualPostControl manualPostControl;
    private TraySupplyer traySupplier;

    private LinkedList<Record> scanResults = new LinkedList<>();
    private FederalPoliceOfficer officer;
    private State currentState;
    private HashMap<String, Byte> permissions;
    private Track[] tracks;
    private Supervision supervision;

    private Map<String, Integer> permissionShift = new HashMap<>();

    private boolean testFlag = false;

    public BaggageScanner(HashMap<String, Byte> permissions) {
        this.permissions = permissions;

        permissionShift.put("scan", 0);
        permissionShift.put("moveForward", 0);
        permissionShift.put("moveBackward", 0);
        permissionShift.put("alarm", 0);
        permissionShift.put("report", 2);
        permissionShift.put("maintenance", 3);
    }

    @Override
    public void scanHandBaggage() {

        if (testFlag) new write().writeTestFile("scanBaggage");

        if (!checkPermissions(permissionShift.get("scan"))) return;

        currentState = currentState.scan();
        scanResults.add(scanner.scan());
        currentState = currentState.scanDone();
    }

    @Override
    public void moveBeltForward() {

        if (testFlag) new write().writeTestFile("moveForward");

        if (!checkPermissions(permissionShift.get("moveForward"))) return;

        System.out.println("Move Belt forward");

        Tray tray = belt.moveForward();
        tray = scanner.move(tray);

        if (tray != null) {
            if (scanResults.getLast().getResult().getItemType() == "CLEAN") {
                if (testFlag) new write().writeTestFile("clean2");
                tracks[1].trayArrive(tray);
            } else {
                if (testFlag) new write().writeTestFile("prohibited1");
                tracks[0].trayArrive(tray);
            }
        }
    }

    @Override
    public void moveBeltBackwards() {

        if (testFlag) new write().writeTestFile("moveBackward");

        if (!checkPermissions(permissionShift.get("moveBackward"))) return;

        Tray tray = tracks[0].getTrays().getLast();
        tray = scanner.move(tray);
        if (tray != null) {
            belt.moveBackwards(tray);
        }
    }

    @Override
    public void alarm() {

        if (!checkPermissions(permissionShift.get("alarm"))) return;

        System.out.println("Alert!!! Prohibited items in the baggage");
        currentState = currentState.lock();

        Passenger passenger = manualPostControl.getTrack().getTrays().getLast().getContainedBaggage().getOwner();

        if (!passenger.isArrested()) {
            tracks[1].callPassenger(passenger);
            officer.arrestPassenger(passenger);
        }

        manualPostControl.setCurrentPassenger(passenger);
        manualPostControl.setCurrentOfficer(new FederalPoliceOfficer[] {this.officer});
        manualPostControl.getCurrentOfficer()[1] = this.officer;
        FederalPoliceOfficer[] inforcement = officer.getOffice().requestReinforcement();

        for (int i = 0; i < inforcement.length; i++) {
            manualPostControl.getCurrentOfficer()[i+1] = inforcement[i];
        }
    }

    @Override
    public void report() {

        if (!checkPermissions(permissionShift.get("report"))) return;

        System.out.println();
        System.out.println("Report Start");
        System.out.println("Report State: " + currentState.getClass().toString());
        System.out.println("Report Scan results: ");
        scanResults.forEach(x ->{
            System.out.println("Report: Scan result: " + x.toString());
        });
        System.out.println("Report End");
    }

    @Override
    public void maintenance() {

        if (!checkPermissions(permissionShift.get("maintenance"))) return;

        System.out.println("Perform maintenance");

        currentState.scansDone();
    }

    public boolean checkPermissions(int shift) {

        String auth = operationStation.getUserType();
        byte value = permissions.get(auth);

        if ((value & 1 << shift) == 0) {
            System.out.println("Unauthorized usage");
            return false;
        }
        return true;
    }

    public boolean checkPermissions(String activity) {
        String auth = operationStation.getUserType();
        switch(auth) {
            case "S":
                switch (activity) {
                    case "report":
                        return true;
                }
                break;

            case "T":
                switch (activity) {
                    case "maintenance":
                        return true;
                }
                break;

            case "I":
                switch (activity) {
                    case "moveForward":
                    case "moveBackward":
                    case "scan":
                    case "alarm":
                        return true;
                }
                break;
        }
        return false;
    }

    public Track[] getTracks() {
        return this.tracks;
    }

    public Tray getTray() {
        return tray;
    }

    public RollerConveyor getRollerConveyor() {
        return rollerConveyor;
    }

    public Belt getBelt() {
        return belt;
    }

    public void setBelt(Belt belt) {
        this.belt = belt;
    }

    public OperationStation getOperationStation() {
        return operationStation;
    }

    public ManualPostControl getManualPostControl() {
        return manualPostControl;
    }

    public LinkedList<data.Record> getScanResults() {
        return new LinkedList<>(scanResults);
    }

    public Scanner getScanner() {
        return this.scanner;
    }

    public FederalPoliceOfficer getOfficer() {
        return officer;
    }

    public void setOfficer(FederalPoliceOfficer officer) {
        this.officer = officer;
    }

    public TraySupplyer getTraySupplier() {
        return traySupplier;
    }

    public Supervision getSupervision() {
        return supervision;
    }

    public void setSupervision(Supervision supervision) {
        this.supervision = supervision;
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public HashMap<String, Byte> getPermissions() {
        return this.permissions;
    }

    public void setOperationStation(OperationStation operationStation) {
        this.operationStation = operationStation;
    }

    public void setTray(Tray tray) {
        this.tray = tray;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setRollerConveyor(RollerConveyor rollerConveyor) {
        this.rollerConveyor = rollerConveyor;
    }

    public void setManualPostControl(ManualPostControl manualPostControl) {
        this.manualPostControl = manualPostControl;
    }

    public void setTraySupplier(TraySupplyer traySupplier) {
        this.traySupplier = traySupplier;
    }

    public void setTracks(Track[] tracks) {
        this.tracks = tracks;
    }

    public Map<String, Integer> getPermissionShift() {
        return permissionShift;
    }

    public void setTestFlag(boolean testFlag) {
        this.testFlag = testFlag;
    }
}
