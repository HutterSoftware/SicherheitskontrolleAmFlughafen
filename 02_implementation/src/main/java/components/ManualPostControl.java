package components;

import explosivedevicecomponents.TraceDetector;
import passenger.Passenger;
import staff.Employee;
import staff.FederalPoliceOfficer;

public class ManualPostControl {
    private BaggageScanner scanner;
    private Track track;
    private TraceDetector detector;
    private Tray tray;

    private Passenger currentPassenger;
    private Employee inspector;
    private FederalPoliceOfficer[] currentOfficer;
    private Tray currentTrayToInvestigate;

    public ManualPostControl(BaggageScanner scanner, Track track, TraceDetector detector) {
        this.scanner = scanner;
        this.track = track;
        this.detector = detector;
    }

    public BaggageScanner getScanner() {
        return scanner;
    }

    public Track getTrack() {
        return track;
    }

    public TraceDetector getDetector() {
        return detector;
    }

    public void setInspector(Employee inspector) {
        this.inspector = inspector;
    }

    public Employee getInspector() {
        return inspector;
    }

    public FederalPoliceOfficer[] getCurrentOfficer() {
        return currentOfficer;
    }

    public void setCurrentOfficer(FederalPoliceOfficer[] currentOfficer) {
        this.currentOfficer = currentOfficer;
    }

    public Tray getTray() {
        return tray;
    }

    public void setTray(Tray tray) {
        this.tray = tray;
    }

    public Passenger getCurrentPassenger() {
        return currentPassenger;
    }

    public void setCurrentPassenger(Passenger currentPassenger) {
        this.currentPassenger = currentPassenger;
    }

    public Tray getCurrentTrayToInvestigate () {
        return currentTrayToInvestigate;
    }

    public void setCurrentTrayToInvestigate (Tray currentTrayToInvestigate) {
        this.currentTrayToInvestigate = currentTrayToInvestigate;
    }
}
