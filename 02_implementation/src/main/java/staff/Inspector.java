package staff;

import State.Locked;
import components.*;
import data.Record;
import data.ScanResult;
import explosivedevicecomponents.TestStrip;
import passenger.HandBaggage;
import passenger.Passenger;
import test.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Inspector extends Employee {
    private boolean isSenior;
    private boolean testFlag = false;

    public Inspector(String id, String name, String birthDate, boolean isSenior) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.isSenior = isSenior;
    }

    public void pushButton(Button button) {
        button.push();

        if (button.getButtonLabel() == Button.BUTTON_LABEL_RECTANGLE) {
            BaggageScanner scanner = button.getStation().getScanner();
            Record record = scanner.getScanResults().getLast();
            if (record.getResult().getItemType() == ScanResult.ITEM_TYPE_CLEAN) {
                System.out.println("Inspector: Baggage is clean");
            } else {
                if (testFlag) new write().writeTestFile("reactToProhibited");
                reactToProhibitedItem(record.getResult().getProhibitedItemType(), scanner);
            }
        }
    }

    public void reactToProhibitedItem(String itemType, BaggageScanner scanner) {
        System.out.println("Inspector: The baggage isn't clean");
        Tray tray = scanner.getScanner().move(null);
        scanner.getTracks()[0].trayArrive(tray);
        scanner.getManualPostControl().setCurrentPassenger(tray.getContainedBaggage().getOwner());
        if (testFlag) new write().writeTestFile("prohibited1");

        switch (itemType) {
            case "knife":
                System.out.println("Insepctor: Knife was found");
                ((Inspector)scanner.getManualPostControl().getInspector()).notifyKnife(scanner);
                break;

            case "weapon":
                System.out.println("Inspector: Weapon was found");
                ((Inspector)scanner.getManualPostControl().getInspector()).notifyWeapon(scanner);
                break;

            case "explosive":
                System.out.println("Inspector: Explosives was found");
                triggerAlert(scanner);

                List<HandBaggage> baggages = Arrays.asList(scanner.getManualPostControl().getCurrentPassenger().getBaggages());
                baggages.remove(tray.getContainedBaggage());

                ((Inspector)scanner.getManualPostControl().getInspector()).
                        testBaggageForExplosiveElements(scanner.getManualPostControl());
                for (HandBaggage baggage: baggages) {
                    Tray removeTray = new Tray();
                    scanner.getTracks()[0].getTrays().remove(baggage);
                    scanner.getTracks()[1].getTrays().remove(baggage);
                }
                break;
        }

        scanner.getManualPostControl().setCurrentOfficer(null);
        scanner.getManualPostControl().setCurrentPassenger(null);
        if (scanner.getCurrentState() instanceof Locked) {
            ((Supervisor)scanner.getSupervision().getEmployee()).unlockBaggageScanner(scanner);
            scanner.getOperationStation().getEmployee().enterPin(scanner.getOperationStation().getReader());
        }
    }

    public void pushTray(BaggageScanner scanner) {
        System.out.println("Inspector: Push tray");
        while (!scanner.getRollerConveyor().getTrayQueue().isEmpty()) {
            scanner.getRollerConveyor().pushTrays();
        }
    }

    public void triggerAlert(BaggageScanner baggageScanner) {
        if (testFlag) new write().writeTestFile("alarm");
        System.out.println("Inspector: Start alarm");
        baggageScanner.alarm();
    }

    public void notifyKnife (BaggageScanner baggageScanner) {
        if (testFlag) new write().writeTestFile("notifyKnife");
        System.out.println("Inspector: Notify knife");
        ManualPostControl manualPostControl = baggageScanner.getManualPostControl();
        Track[] tracks = baggageScanner.getTracks();
        manualPostControl.setCurrentTrayToInvestigate(tracks[0].getTrays().removeLast());
        Passenger passenger = manualPostControl.getCurrentTrayToInvestigate().getContainedBaggage().getOwner();

        // Call passenger
        tracks[1].callPassenger(passenger);
        manualPostControl.setCurrentPassenger(passenger);

        ScanResult result = baggageScanner.getScanResults().getLast().getResult();
        String extractedString = manualPostControl.getCurrentTrayToInvestigate().getContainedBaggage().takeContent(
                result.getPosition()[0], result.getPosition()[1], result.getItemType().length());

        baggageScanner.getTracks()[1].passengerWaiting(passenger);
        manualPostControl.setCurrentPassenger(null);

        tracks[0].getTrays().add(manualPostControl.getCurrentTrayToInvestigate());
        manualPostControl.setCurrentTrayToInvestigate(null);

        // Move belt
        ((Inspector)baggageScanner.getOperationStation().getEmployee()).pushButton(baggageScanner.getOperationStation().getButtons()[0]);
        ((Inspector)baggageScanner.getOperationStation().getEmployee()).pushButton(baggageScanner.getOperationStation().getButtons()[1]);

    }

    public void notifyWeapon(BaggageScanner baggageScanner) {
        System.out.println("Inspector: Weapon was notified");
        ManualPostControl manualPostControl = baggageScanner.getManualPostControl();
        manualPostControl.setCurrentTrayToInvestigate(manualPostControl.getTrack().getTrays().getLast());
        HandBaggage baggage = manualPostControl.getCurrentTrayToInvestigate().takeBaggage();

        Supervisor supervisor = (Supervisor) baggageScanner.getSupervision().getEmployee();
        List<HandBaggage> baggages = Arrays.stream(baggageScanner.getManualPostControl().getCurrentPassenger().
                getBaggages()).collect(Collectors.toList());

        baggages.remove(baggage);
        ScanResult result = baggageScanner.getScanResults().get(baggageScanner.getScanResults().size() - 1).getResult();
        String string = baggage.takeContent(result.getPosition()[0], result.getPosition()[1], result.getItemType().length());

        System.out.println("Inspector took " + string + " out of the baggage of passenger " +
                manualPostControl.getCurrentPassenger().getName());

        manualPostControl.getCurrentOfficer()[2].takeWeapon(string);
        manualPostControl.getCurrentTrayToInvestigate().putBaggage(baggage);

        manualPostControl.getTrack().getTrays().add(manualPostControl.getCurrentTrayToInvestigate());
        manualPostControl.setCurrentTrayToInvestigate(null);

        //Unlock
        ((Supervisor)baggageScanner.getSupervision().getEmployee()).unlockBaggageScanner(baggageScanner);
        baggageScanner.getOperationStation().getEmployee().enterPin(baggageScanner.getOperationStation().getReader());

        while (!baggageScanner.getBelt().getTrayQueue().isEmpty()) {
            ((Inspector)baggageScanner.getOperationStation().getEmployee()).pushButton(baggageScanner.getOperationStation().getButtons()[2]);
            ((Inspector)baggageScanner.getOperationStation().getEmployee()).pushButton(baggageScanner.getOperationStation().getButtons()[1]);
        }

        ((Inspector)baggageScanner.getOperationStation().getEmployee()).pushButton(baggageScanner.getOperationStation().getButtons()[2]);
        System.out.println("Inspector: All baggage of passengers was checked");

        for (HandBaggage baggagee : baggages) {
            Tray removalTray = new Tray(baggagee);
            baggageScanner.getBelt().getTrayQueue().remove(removalTray);
            baggageScanner.getTracks()[0].getTrays().remove(removalTray);
            baggageScanner.getTracks()[1].getTrays().remove(removalTray);
        }
    }

    public boolean isSenior() {
        return this.isSenior;
    }

    public void testBaggageForExplosiveElements(ManualPostControl manualPostControl) {
        if (testFlag) new write().writeTestFile("testExplosive");
        System.out.println("Inspector: Testing for explosive items");
        manualPostControl.setCurrentTrayToInvestigate(manualPostControl.getTrack().getTrays().removeLast());

        TestStrip stripe = manualPostControl.getCurrentTrayToInvestigate().getContainedBaggage().swipeTest();
        boolean result = manualPostControl.getDetector().testStripe(stripe);

        if (result) {
            System.out.println("Inspector: Explosives found");
            FederalPoliceOfficer explosivesOfficer = Arrays.stream(manualPostControl.getCurrentOfficer()).
                    filter(federalPoliceOfficer -> federalPoliceOfficer.getId().equals("O2")).findFirst().orElseThrow();

            explosivesOfficer.steerRobot(explosivesOfficer.getOffice().getDisarmRobot(), manualPostControl.getScanner());
        } else {
            System.out.println("Inspector: Explosives not found");
        }
    }

    public void setTestFlag(boolean testFlag) {
        this.testFlag = testFlag;
    }
}
