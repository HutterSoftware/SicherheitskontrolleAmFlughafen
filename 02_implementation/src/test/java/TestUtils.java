import components.Scanner;
import components.Tray;

import passenger.HandBaggage;
import passenger.Layer;
import passenger.Passenger;

import simulation.Simulation;

import staff.FederalPoliceOfficer;
import staff.Inspector;

import java.io.*;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class TestUtils {

    /**
     * create a TestBaggage
     * @param filename filename of the TestBaggage
     * @return the TestBaggage
     * @throws IOException IOException
     * @throws URISyntaxException URISyntaxException
     */
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

    /**
     * scan the TestBaggage
     * @param baggage the Baggage to scan
     * @param scanner the scanner to scan the Baggage with
     * @return record of the scan
     */
    public static data.Record scanBaggage(HandBaggage baggage, Scanner scanner) {

        Tray tray = new Tray();
        tray.insertBaggage(baggage);
        scanner.move(tray);

        return scanner.scan();
    }

    /**
     * remove [ and ] from a string
     * @param prohibited a string with [ and/or ]
     * @return string without [ and ]
     */
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

    /**
     * write an empty string to the test File
     * @throws IOException IOException
     */
    public static void clearProcedureTestFile() throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/Procedure.txt", false));
        writer.write("");
        writer.close();
    }

    /**
     * set a Passenger to test the simulation with
     * @param simulation the Simulation which is tested
     * @param filename the filename of the Baggage of the test Passenger
     * @throws IOException IOException
     * @throws URISyntaxException URISyntaxexception
     */
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

    /**
     * set the TestFlag in the simulation
     * @param simulation the simulation which is tested
     */
    public static void setTestFlag(Simulation simulation) {

        simulation.getScanner().setTestFlag(true);
        ((Inspector)simulation.getEmployees().get("I2")).setTestFlag(true);
        ((Inspector)simulation.getEmployees().get("I3")).setTestFlag(true);
        simulation.getPassengerList().get(0).getBaggages()[0].setTestFlag(true);
        ((FederalPoliceOfficer)simulation.getEmployees().get("O1")).setTestFlag(true);
        ((FederalPoliceOfficer)simulation.getEmployees().get("O2")).setTestFlag(true);
        ((FederalPoliceOfficer)simulation.getEmployees().get("O3")).setTestFlag(true);
        ((FederalPoliceOfficer)simulation.getEmployees().get("O1")).getOffice().setTestFlag(true);
    }

    /**
     * load the expected procedure from a file
     * @param procedureNumber the number of the procedure which is tested
     *                        (0: no prohibited Item, 1: Knife, 2: Weapon, 3: Explosive)
     * @return a string with the correct procedure
     * @throws IOException IOException
     * @throws URISyntaxException URISyntaxException
     */
    public static String readCorrectProcedure(int procedureNumber) throws IOException, URISyntaxException {

        File procedure = new File(TestUtils.class.getResource("correct_procedure.txt").toURI());
        BufferedReader reader = new BufferedReader(new FileReader(procedure));
        String line = reader.readLine();

        for (int i = 0; i < procedureNumber; i++) {
            line = reader.readLine();
        }

        return line;
    }

    /**
     * load the actual procedure from a file
     * @return a string with the actual procedure
     * @throws IOException IOException
     * @throws URISyntaxException URISyntaxException
     */
    public static String readActualProcedure() throws IOException, URISyntaxException {

        File procedure = new File(Thread.currentThread().getContextClassLoader().getResource("Procedure.txt").toURI());
        BufferedReader reader = new BufferedReader(new FileReader(procedure));

        return reader.readLine();
    }
}
