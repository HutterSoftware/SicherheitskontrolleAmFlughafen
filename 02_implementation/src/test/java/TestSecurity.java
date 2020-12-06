import algorithms.AES;

import components.BaggageScanner;
import components.Scanner;

import data.ScanResult;

import passenger.HandBaggage;
import passenger.Passenger;

import simulation.Configuration;
import simulation.Simulation;

import staff.Employee;
import staff.Supervisor;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.URISyntaxException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestSecurity {

    private Simulation simulation;
    private Configuration configuration;

    /**
     * create simulation
     */
    @BeforeEach
    public void createSimulation() {
        Simulation.Builder builder = new Simulation.Builder();
        this.configuration = builder.getConfiguration();

        builder.defaultEmployees();
        builder.defaultPassengers();
        this.simulation = builder.build();
        this.simulation.initializeSimulation();
    }

    /**
     * test if there are 568 passengers
     * test if every passenger has the correct amount of baggages
     * test if prohibited items are in right baggage
     * test if prohibited items are in right layer of baggage
     * @param passengerIndex the index of the passenger
     * @param baggageIndex index of first baggage of passenger (not used)
     * @param name the name of the passenger
     * @param numberOfBaggages number of baggages of passenger
     * @param prohibitedItem1 prohibitedItem1 or - (example: [K,1,2])
     * @param prohibitedItem2 prohibitedItem2 or -
     */
    @Order(1)
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

    /**
     * test if every position of BaggageScanner has the correct type of employee
     */
    @Order(2)
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

    /**
     * idCard should be locked if employee typed wrong pin three times
     */
    @Order(3)
    @Test
    public void lockIDCardTest() {

        Employee inspector = this.simulation.getEmployees().get("I1");

        for (int i = 0; i < 3; i++) {
            inspector.enterPin(this.simulation.getScanner().getOperationStation().getReader(), "wrongPin");
        }

        assertTrue(inspector.getIdCard().isLocked());
    }

    /**
     * profile K and profile O should not be able to login at BaggageScanner
     */
    @Order(4)
    @Test
    public void scannerLoginTest() {

        Employee officer = this.simulation.getEmployees().get("O1");
        officer.enterPin(this.simulation.getScanner().getOperationStation().getReader());

        assertEquals("", simulation.getScanner().getOperationStation().getAuthentication());

        Employee houseKeeping = this.simulation.getEmployees().get("K");
        houseKeeping.enterPin(this.simulation.getScanner().getOperationStation().getReader());

        assertEquals("", simulation.getScanner().getOperationStation().getAuthentication());
    }

    /**
     * test if employee is only able to do his functions
     * moveBeltForward  -> Inspector
     * moveBeltBackward -> Inspector
     * scanHandBaggage  -> Inspector
     * alarm            -> Inspector
     * report           -> Supervisor
     * maintenance      -> Technician
     */
    @Order(5)
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

    /**
     * only a Supervisor should be able to unlock a (locked) BaggageScanner
     */
    @Order(6)
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

    /**
     * Record/Result should contain
     * PROHIBITED -> ProhibitedItem
     * KNIFE      -> knife
     */
    @Order(7)
    @Test
    public void knifeTest() throws IOException, URISyntaxException {

        HandBaggage baggage = TestUtils.createBaggage("knife_baggage.txt");

        Scanner scanner = this.simulation.getScanner().getScanner();

        data.Record record = TestUtils.scanBaggage(baggage, scanner);
        ScanResult result = record.getResult();

        assertEquals("PROHIBITED", result.getItemType());
        assertEquals("knife", result.getProhibitedItemType());
    }

    /**
     * Record/Result should contain
     * PROHIBITED -> ProhibitedItem
     * WEAPON     -> weapon
     */
    @Order(8)
    @Test
    public void weaponTest() throws IOException, URISyntaxException {

        HandBaggage baggage = TestUtils.createBaggage("weapon_baggage.txt");

        Scanner scanner = this.simulation.getScanner().getScanner();

        data.Record record = TestUtils.scanBaggage(baggage, scanner);
        ScanResult result = record.getResult();

        assertEquals("PROHIBITED", result.getItemType());
        assertEquals("weapon", result.getProhibitedItemType());
    }

    /**
     * Record/Result should contain
     * PROHIBITED -> ProhibitedItem
     * EXPLOSIVE  -> explosive
     */
    @Order(9)
    @Test
    public void explosiveTest() throws IOException, URISyntaxException {

        HandBaggage baggage = TestUtils.createBaggage("explosive_baggage.txt");

        Scanner scanner = this.simulation.getScanner().getScanner();

        data.Record record = TestUtils.scanBaggage(baggage, scanner);
        ScanResult result = record.getResult();

        assertEquals("PROHIBITED", result.getItemType());
        assertEquals("explosive", result.getProhibitedItemType());
    }

    /**
     * every scan should generate a record
     * @param passengerIndex the index of the passenger
     * @param baggageIndex index of first baggage of passenger (not used)
     * @param name the name of the passenger
     * @param numberOfBaggages number of baggages of passenger
     * @param prohibitedItem1 prohibitedItem1 or - (example: [K,1,2])
     * @param prohibitedItem2 prohibitedItem2 or -
     */
    @Order(10)
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

    /**
     * procedure for normal baggage
     */
    @Order(11)
    @Test
    public void noProhibitedProcedureTest() throws IOException, URISyntaxException {

        TestUtils.clearProcedureTestFile();
        TestUtils.setTestPassenger(this.simulation, "normal_baggage.txt");
        TestUtils.setTestFlag(this.simulation);

        this.simulation.run();

        String correct = TestUtils.readCorrectProcedure(0);
        String actual = TestUtils.readActualProcedure();

        assertEquals(correct, actual);

        TestUtils.clearProcedureTestFile();
    }

    /**
     * procedure for knife baggage
     */
    @Order(12)
    @Test
    public void knifeProcessTest() throws IOException, URISyntaxException {

        TestUtils.clearProcedureTestFile();
        TestUtils.setTestPassenger(this.simulation, "knife_baggage.txt");
        TestUtils.setTestFlag(this.simulation);

        this.simulation.run();

        String correct = TestUtils.readCorrectProcedure(1);
        String actual = TestUtils.readActualProcedure();

        assertEquals(correct, actual);

        TestUtils.clearProcedureTestFile();
    }

    /**
     * procedure for weapon baggage
     */
    @Order(13)
    @Test
    public void weaponProcedureTest() throws IOException, URISyntaxException {

        TestUtils.clearProcedureTestFile();
        TestUtils.setTestPassenger(this.simulation, "weapon_baggage.txt");
        TestUtils.setTestFlag(this.simulation);

        this.simulation.run();

        String correct = TestUtils.readCorrectProcedure(2);
        String actual = TestUtils.readActualProcedure();

        assertEquals(correct, actual);

        TestUtils.clearProcedureTestFile();
    }

    /**
     * procedure for explosive baggage
     */
    @Order(14)
    @Test
    public void explosivesProcedureTest() throws IOException, URISyntaxException {

        TestUtils.clearProcedureTestFile();
        TestUtils.setTestPassenger(this.simulation, "explosive_baggage.txt");
        TestUtils.setTestFlag(this.simulation);

        this.simulation.run();

        String correct = TestUtils.readCorrectProcedure(3);
        String actual = TestUtils.readActualProcedure();

        assertEquals(correct, actual);

        TestUtils.clearProcedureTestFile();
    }

    /**
     * run the simulation
     * there should be no passengers left
     * BaggageScanner should be shutdown
     */
    @Order(15)
    @Test
    public void simulation() {
        this.simulation.run();

        assertTrue(simulation.getScanner().getTraySupplier().getPassengers().isEmpty());
        assertEquals("Shutdown", simulation.getScanner().getCurrentState().getClass().getSimpleName());
    }
}
