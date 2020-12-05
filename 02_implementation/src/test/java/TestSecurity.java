import components.BaggageScanner;
import components.Scanner;
import components.Tray;
import data.Record;
import data.ScanResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import passenger.HandBaggage;
import passenger.Layer;
import passenger.Passenger;
import simulation.Configuration;
import simulation.Simulation;
import staff.Employee;
import staff.FederalPoliceOfficer;
import staff.Inspector;
import algorithms.AES;


import java.io.*;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestSecurity {

    private Simulation simulation;
    private Configuration configuration;

    @BeforeEach
    public void createSimulation() {
        Simulation.Builder builder = new Simulation.Builder();
        this.configuration = builder.getConfiguration();

        builder.defaultEmployees();
//        builder.defaultPassengers();
        this.simulation = builder.build();
        this.simulation.initializeSimulation();
    }

    @ParameterizedTest()
    @CsvFileSource(resources = "passenger_baggage.txt", delimiter = ';')
    public void simulationTest(String name, int numberOfBaggages, String prohibitedItems) {

    }

    @Test
    public void employeePositionTest() throws IOException, URISyntaxException {

        BaggageScanner scanner = this.simulation.getScanner();

        AES aes = new AES(this.configuration.getKey());

        Employee rollerConveyor = scanner.getRollerConveyor().getWorkingInspector();
        String rCStripeContent = aes.decrypt(rollerConveyor.getIdCard().getMagnetStripe());

        aes = new AES(this.configuration.getKey());
        Employee operationStation = scanner.getOperationStation().getEmployee();
        String oSStripeContent = aes.decrypt(operationStation.getIdCard().getMagnetStripe());

        aes = new AES(this.configuration.getKey());
        Employee manualPostControl = scanner.getManualPostControl().getInspector();
        String mPCStripeContent = aes.decrypt(manualPostControl.getIdCard().getMagnetStripe());

        aes = new AES(this.configuration.getKey());
        Employee supervision = scanner.getSupervision().getEmployee();
        String sStripeContent = aes.decrypt(supervision.getIdCard().getMagnetStripe());

        aes = new AES(this.configuration.getKey());
        Employee officer = scanner.getOfficer();
        String oStripeContent = aes.decrypt(officer.getIdCard().getMagnetStripe());


        assertEquals("I", rCStripeContent.split("\\*\\*\\*")[1]);
        assertEquals("I", oSStripeContent.split("\\*\\*\\*")[1]);
        assertEquals("I", mPCStripeContent.split("\\*\\*\\*")[1]);
        assertEquals("S", sStripeContent.split("\\*\\*\\*")[1]);
        assertEquals("O", oStripeContent.split("\\*\\*\\*")[1]);
    }

    @Test
    public void lockIDCardTest() throws IOException, URISyntaxException {

        Employee inspector = this.simulation.getEmployees().get("I1");

        for (int i = 0; i < 3; i++) {
            inspector.enterPin(this.simulation.getScanner().getOperationStation().getReader(), "wrongPin");
        }

        assertTrue(inspector.getIdCard().isLocked());
    }

    @Test
    public void scannerLoginTest() {

        Employee officer = this.simulation.getEmployees().get("O1");
        officer.enterPin(this.simulation.getScanner().getOperationStation().getReader());

        System.out.println(simulation.getScanner().getOperationStation().getAuthentication());

        assertEquals("", simulation.getScanner().getOperationStation().getAuthentication());
    }

    @Test
    public void employeeProfileTest() {

    }

    @Test
    public void unlockScannerTest() {

    }

    @Test
    public void knifeTest() throws IOException, URISyntaxException {

        HandBaggage baggage = TestUtils.createBaggage("knife_baggage.txt");

        Scanner scanner = this.simulation.getScanner().getScanner();

        data.Record record = TestUtils.scanBaggage(baggage, scanner);
        ScanResult result = record.getResult();

        assertEquals("PROHIBITED", result.getItemType());
        assertEquals("kn!fe", result.getProhibitedItemType());
    }

    @Test
    public void weaponTest() throws IOException, URISyntaxException {

        HandBaggage baggage = TestUtils.createBaggage("weapon_baggage.txt");

        Scanner scanner = this.simulation.getScanner().getScanner();

        data.Record record = TestUtils.scanBaggage(baggage, scanner);
        ScanResult result = record.getResult();

        assertEquals("PROHIBITED", result.getItemType());
        assertEquals("glock|7", result.getProhibitedItemType());
    }

    @Test
    public void explosiveTest() throws IOException, URISyntaxException {

        HandBaggage baggage = TestUtils.createBaggage("explosive_baggage.txt");

        Scanner scanner = this.simulation.getScanner().getScanner();

        data.Record record = TestUtils.scanBaggage(baggage, scanner);
        ScanResult result = record.getResult();

        assertEquals("PROHIBITED", result.getItemType());
        assertEquals("exp|os!ve", result.getProhibitedItemType());
    }

    @Test
    public void scanRecordTest() {

    }

    @Test
    public void noProhibitedProcessTest() {

    }

    @Test
    public void knifeProcessTest() {

    }

    @Test
    public void weaponProcessTest() {

    }

    @Test
    public void explosivesProcessTest() {

    }
}
