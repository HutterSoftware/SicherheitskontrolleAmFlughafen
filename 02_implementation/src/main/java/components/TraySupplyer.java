package components;

import passenger.Passenger;

import java.util.ArrayDeque;
import java.util.Deque;

public class TraySupplyer {
    private Deque<Passenger> passengers = new ArrayDeque<>(568);
    private BaggageScanner baggageScanner;

    public TraySupplyer(BaggageScanner baggageScanner) {
        this.baggageScanner = baggageScanner;
    }

    public void nextPassenger() {
        Passenger currentPassenger = passengers.pollFirst();

        currentPassenger.putBaggagesToScan(baggageScanner);
        baggageScanner.getTracks()[1].getWaitingPassengers();
    }

    public Tray getTray() {
        return new Tray();
    }

    public Deque<Passenger> getPassengers() {
        return this.passengers;
    }
}
