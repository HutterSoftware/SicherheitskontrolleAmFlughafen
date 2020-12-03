package passenger;

import components.BaggageScanner;
import components.Tray;

public class Passenger {
    private boolean isArrested;
    private String name;
    private HandBaggage[] baggages;

    public Passenger(String name) {
        this.name = name;
    }



    public void putBaggagesToScan(BaggageScanner baggageScanner) {
        for (int i = 0; i < baggages.length; i++) {
            Tray tray = baggageScanner.getTraySupplier().getTray();
            tray.insertBaggage(baggages[i]);
            baggageScanner.getRollerConveyor().addTray(tray);
        }
    }

    public void setArrested(boolean arrested) {
        isArrested = arrested;
    }

    public boolean isArrested() {
        return this.isArrested;
    }

    public HandBaggage[] getBaggages() {
        return baggages;
    }

    public void setBaggages(HandBaggage[] baggages) {
        this.baggages = baggages;
    }

    public String getName() {
        return name;
    }
}
