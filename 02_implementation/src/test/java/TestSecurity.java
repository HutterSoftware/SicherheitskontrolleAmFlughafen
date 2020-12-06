import algorithms.AES;

import components.BaggageScanner;
import components.Scanner;

import data.ScanResult;

import passenger.HandBaggage;

import passenger.Layer;
import passenger.Passenger;
import simulation.Configuration;
import simulation.Simulation;

import staff.Employee;
import staff.Supervisor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class TestSecurity {

    private Simulation simulation;
    private Configuration configuration;

    @BeforeEach
    public void createSimulation() {
        Simulation.Builder builder = new Simulation.Builder();
        this.configuration = builder.getConfiguration();

        builder.defaultEmployees();
        builder.defaultPassengers();
        this.simulation = builder.build();
        this.simulation.initializeSimulation();
    }

    @ParameterizedTest()
    @CsvFileSource(resources = "passenger_baggage_index.txt", delimiter = ';')
    public void simulationTest(int passengerIndex, int baggageIndex, String name, int numberOfBaggages, String prohibitedItem1, String prohibitedItem2) {

        Passenger passenger = this.simulation.getPassengerList().get(passengerIndex);

        assertEquals(name, passenger.getName());
        assertEquals(numberOfBaggages, passenger.getBaggages().length);

        for (int i = 0; i < numberOfBaggages; i++) {

            HandBaggage baggage = passenger.getBaggages()[i];
            data.Record record = TestUtils.scanBaggage(baggage, this.simulation.getScanner().getScanner());

            String pItem1 = TestUtils.getProhibitedItemString(prohibitedItem1);
            String[] prohibitedItemInformation1 = pItem1.split(",");

            String pItem2 = TestUtils.getProhibitedItemString(prohibitedItem2);
            String[] prohibitedItemInformation2 = pItem2.split(",");

            if (!prohibitedItem1.equals("-") && Integer.parseInt(prohibitedItemInformation1[1]) - 1 == i) {

                char prohibitedItemType = record.getResult().getProhibitedItemType().charAt(0);

                assertEquals("PROHIBITED", record.getResult().getItemType());
                assertEquals(prohibitedItemInformation1[0].toLowerCase().charAt(0), prohibitedItemType);
                assertEquals(Integer.parseInt(prohibitedItemInformation1[2]) - 1, record.getResult().getPosition()[0]);

            } else if (!prohibitedItem2.equals("-") && Integer.parseInt(prohibitedItemInformation2[1]) - 1 == i) {

                char prohibitedItemType = record.getResult().getProhibitedItemType().charAt(0);

                assertEquals("PROHIBITED", record.getResult().getItemType());
                assertEquals(prohibitedItemInformation2[0].toLowerCase().charAt(0), prohibitedItemType);
                assertEquals(Integer.parseInt(prohibitedItemInformation2[2]) - 1, record.getResult().getPosition()[0]);

            } else {

                assertEquals("CLEAN", record.getResult().getItemType());
            }
        }
    }

    @Test
    public void employeePositionTest() {

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
    public void lockIDCardTest() {

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

        assertEquals("", simulation.getScanner().getOperationStation().getAuthentication());

        Employee houseKeeping = this.simulation.getEmployees().get("K");
        houseKeeping.enterPin(this.simulation.getScanner().getOperationStation().getReader());

        assertEquals("", simulation.getScanner().getOperationStation().getAuthentication());
    }

    @Test
    public void employeeProfileTest() {

        BaggageScanner scanner = simulation.getScanner();
        scanner.getSupervision().pressPowerButton();

        System.out.println("\nInspector");
        scanner.getOperationStation().getEmployee().enterPin(scanner.getOperationStation().getReader());

        assertTrue(scanner.checkPermissions("moveForward"));
        assertTrue(scanner.checkPermissions("moveBackward"));
        assertTrue(scanner.checkPermissions("scan"));
        assertTrue(scanner.checkPermissions("alarm"));
        assertFalse(scanner.checkPermissions("report"));
        assertFalse(scanner.checkPermissions("maintenance"));


        System.out.println("\nSupervisor");
        scanner.getOperationStation().setEmployee(simulation.getEmployees().get("S"));
        scanner.getOperationStation().getEmployee().enterPin(scanner.getOperationStation().getReader());

        assertFalse(scanner.checkPermissions("moveForward"));
        assertFalse(scanner.checkPermissions("moveBackward"));
        assertFalse(scanner.checkPermissions("scan"));
        assertFalse(scanner.checkPermissions("alarm"));
        assertTrue(scanner.checkPermissions("report"));
        assertFalse(scanner.checkPermissions("maintenance"));


        System.out.println("\nMaintenance");
        scanner.getOperationStation().setEmployee(simulation.getEmployees().get("T"));
        scanner.getOperationStation().getEmployee().enterPin(scanner.getOperationStation().getReader());

        assertFalse(scanner.checkPermissions("moveForward"));
        assertFalse(scanner.checkPermissions("moveBackward"));
        assertFalse(scanner.checkPermissions("scan"));
        assertFalse(scanner.checkPermissions("alarm"));
        assertFalse(scanner.checkPermissions("report"));
        assertTrue(scanner.checkPermissions("maintenance"));
    }

    @Test
    public void unlockScannerTest() {

        BaggageScanner scanner = simulation.getScanner();

        scanner.getSupervision().pressPowerButton();
        scanner.getOperationStation().getEmployee().enterPin(scanner.getOperationStation().getReader());
        scanner.setCurrentState(scanner.getCurrentState().lock());


        System.out.println("\nInspector");
        scanner.getOperationStation().getEmployee().enterPin(scanner.getOperationStation().getReader());
        assertEquals("Locked", scanner.getCurrentState().getClass().getSimpleName());


        System.out.println("\nTechnician");
        scanner.getOperationStation().setEmployee(simulation.getEmployees().get("T"));
        scanner.getOperationStation().getEmployee().enterPin(scanner.getOperationStation().getReader());
        assertEquals("Locked", scanner.getCurrentState().getClass().getSimpleName());


        System.out.println("\nSupervisor");
        ((Supervisor)simulation.getEmployees().get("S")).unlockBaggageScanner(scanner);
        assertEquals("Activated", scanner.getCurrentState().getClass().getSimpleName());
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
        assertEquals("weapon", result.getProhibitedItemType());
    }

    @Test
    public void explosiveTest() throws IOException, URISyntaxException {

        HandBaggage baggage = TestUtils.createBaggage("explosive_baggage.txt");

        Scanner scanner = this.simulation.getScanner().getScanner();

        data.Record record = TestUtils.scanBaggage(baggage, scanner);
        ScanResult result = record.getResult();

        assertEquals("PROHIBITED", result.getItemType());
        assertEquals("explosive", result.getProhibitedItemType());
    }

    @ParameterizedTest()
    @CsvFileSource(resources = "passenger_baggage_index.txt", delimiter = ';')
    public void scanRecordTest(int passengerIndex, int baggageIndex, String name, int numberOfBaggages, String prohibitedItem1, String prohibitedItem2) {

        Passenger passenger = this.simulation.getPassengerList().get(passengerIndex);

        for (int i = 0; i < numberOfBaggages; i++) {

            HandBaggage baggage = passenger.getBaggages()[i];
            data.Record record = TestUtils.scanBaggage(baggage, this.simulation.getScanner().getScanner());

            assertNotNull(record.getTimestamp());
            assertNotNull(record.getResult());
            assertNotNull(record.getResult().getItemType());

            if (record.getResult().getItemType().equals("PROHIBITED")) {

                assertNotNull(record.getResult().getProhibitedItemType());
            }
        }
    }

    @Test
    public void noProhibitedProcedureTest() throws IOException, URISyntaxException {

        TestUtils.clearProcedureTestFile();
        TestUtils.setTestPassenger(this.simulation, "normal_baggage.txt");
        TestUtils.setTestFlag(this.simulation);

        this.simulation.run();


        //File f = new File(Thread.currentThread().getContextClassLoader().getResource("001_baggage.txt").toURI());
    }

    @Test
    public void knifeProcessTest() throws IOException, URISyntaxException {

        TestUtils.clearProcedureTestFile();
        TestUtils.setTestPassenger(this.simulation, "knife_baggage.txt");
        TestUtils.setTestFlag(this.simulation);

        this.simulation.run();
    }

    @Test
    public void weaponProcedureTest() throws IOException, URISyntaxException {

        TestUtils.clearProcedureTestFile();
        TestUtils.setTestPassenger(this.simulation, "weapon_baggage.txt");
        TestUtils.setTestFlag(this.simulation);

        this.simulation.run();
    }

    @Test
    public void explosivesProcedureTest() throws IOException, URISyntaxException {

        TestUtils.clearProcedureTestFile();
        TestUtils.setTestPassenger(this.simulation, "explosive_baggage.txt");
        TestUtils.setTestFlag(this.simulation);

        this.simulation.run();
    }

    @Test
    public void simulation() {
        this.simulation.run();
    }
}
