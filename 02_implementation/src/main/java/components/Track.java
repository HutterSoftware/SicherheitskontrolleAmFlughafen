package components;

import passenger.HandBaggage;
import passenger.Passenger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Track {
    private LinkedList<Tray> trays = new LinkedList<>();
    private int trackNumber;
    private List<Passenger> waitingPassengers = new LinkedList<>();

    public Track(int trackNumber) {
        this.trackNumber = trackNumber;
    }


    public LinkedList<Tray> getTrays() {
        return trays;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public List<Passenger> getWaitingPassengers() {
        return waitingPassengers;
    }

    public void callPassenger(Passenger passenger) {
        if (waitingPassengers.contains(passenger)) {
            waitingPassengers.remove(passenger);
            System.out.println("Passenger " + passenger.getName() + " was called");
        }
    }

    public void trayArrive(Tray tray) {
        trays.add(tray);

        Passenger passenger = tray.getContainedBaggage().getOwner();
        List <HandBaggage> baggage =trays.stream().map(Tray::getContainedBaggage).collect(Collectors.toList());

        if (baggage.containsAll(Arrays.asList(passenger.getBaggages()))) {
            if (waitingPassengers.contains(passenger)) {
                passengerLeave(passenger, passenger.getBaggages());
            }
        }
        System.out.println("Tray arrive");
    }
    
    public void passengerLeave(Passenger passenger, HandBaggage[] baggage) {
        List<Tray> removingTrays = trays.stream()
                .filter(tray -> Arrays.stream(baggage).anyMatch(bag -> bag.equals(tray.getContainedBaggage())))
                .collect(Collectors.toList());

        waitingPassengers.remove(passenger);
        trays.removeAll(removingTrays);
        System.out.println("Removing passengers with every hand baggage");
    }

    public void passengerWaiting(Passenger passenger) {
        waitingPassengers.add(passenger);
    }
}
