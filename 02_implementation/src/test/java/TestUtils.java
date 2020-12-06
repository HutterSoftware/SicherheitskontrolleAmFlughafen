import components.Scanner;
import components.Tray;
import passenger.HandBaggage;
import passenger.Layer;
import passenger.Passenger;
import simulation.Configuration;
import simulation.Simulation;
import staff.FederalPoliceOfficer;
import staff.Inspector;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TestUtils {

    public static HandBaggage createBaggage(String filename) throws IOException, URISyntaxException {

        Passenger passenger = new Passenger("Theo Tester");

        File baggageFile = new File(TestUtils.class.getResource(filename).toURI());
        BufferedReader baggageReader = new BufferedReader(new FileReader(baggageFile));
        Layer[] layers = new Layer[5];
        for (int j = 0; j < 5; j++) {
            char[] line = baggageReader.readLine().toCharArray();
            layers[j] = new Layer(line);
        }

        return new HandBaggage(passenger, layers);
    }

    public static data.Record scanBaggage(HandBaggage baggage, Scanner scanner) {

        Tray tray = new Tray();
        tray.insertBaggage(baggage);
        scanner.move(tray);

        return scanner.scan();
    }

    public static String getProhibitedItemString (String prohibited) {
        String prohibitedItem = "";
        if (prohibited.contains("[")) {
            prohibitedItem = prohibited.split("\\[")[1];
        } else {
            prohibitedItem = prohibited;
        }
        prohibitedItem = prohibitedItem.split("]")[0];
        return prohibitedItem;
    }

    public static void clearProcedureTestFile() throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/Procedure.txt", false));
        writer.write("");
        writer.close();
    }

    public static void setTestPassenger(Simulation simulation, String filename) throws IOException, URISyntaxException {

        List<Passenger> passengers = new LinkedList<>();
        HandBaggage[] baggages = {createBaggage(filename)};
        Passenger passenger = baggages[0].getOwner();
        passenger.setBaggages(baggages);
        passengers.add(passenger);
        simulation.setPassengerList(passengers);

        simulation.getScanner().getTraySupplier().getPassengers().clear();
        simulation.initializeSimulation();
    }

    public static void setTestFlag(Simulation simulation) {

        simulation.getScanner().setTestFlag(true);
        ((Inspector)simulation.getEmployees().get("I2")).setTestFlag(true);
        ((Inspector)simulation.getEmployees().get("I3")).setTestFlag(true);
        simulation.getPassengerList().get(0).getBaggages()[0].setTestFlag(true);
        ((FederalPoliceOfficer)simulation.getEmployees().get("O1")).setTestFlag(true);
        ((FederalPoliceOfficer)simulation.getEmployees().get("O1")).getOffice().setTestFlag(true);
    }
}
