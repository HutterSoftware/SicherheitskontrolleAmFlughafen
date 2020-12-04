package simulation;

import algorithms.AES;
import components.*;
import components.Button;
import components.Scanner;
import explosivedevicecomponents.TraceDetector;
import passenger.HandBaggage;
import passenger.Layer;
import passenger.Passenger;
import staff.*;

import java.io.*;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.*;
import java.util.List;

public class Simulation {

    private BaggageScanner scanner;
    private List<Passenger> passengerList;
    private Map<String, Employee> employees;
    private FederalPoliceOffice policeOffice;

    public Simulation(BaggageScanner scanner,
                      List<Passenger> passengers,
                      Map<String, Employee> employees,
                      FederalPoliceOffice office) {

        this.scanner = scanner;
        this.passengerList = passengers;
        this.employees = employees;
        this.policeOffice = office;
    }

    public void initializeSimulation() {
        scanner.getRollerConveyor().setWorkingInspector(employees.get("I1"));
        scanner.getOperationStation().setEmployee(employees.get("I2"));
        scanner.getManualPostControl().setInspector(employees.get("I3"));
        scanner.getSupervision().setEmployee(employees.get("S"));
        scanner.setOfficer((FederalPoliceOfficer) employees.get("O1"));
        scanner.getTraySupplier().getPassengers().addAll(passengerList);
    }

    public void run() {
        scanner.getSupervision().pressPowerButton();

        scanner.getOperationStation().getEmployee().enterPin(scanner.getOperationStation().getReader());
        while (!scanner.getTraySupplier().getPassengers().isEmpty()) {
            System.out.println("Scan all baggages of passenger");
            scanner.getTraySupplier().nextPassenger();
            ((Inspector)employees.get("I1")).pushTray(scanner);
            while (!scanner.getBelt().getTrayQueue().isEmpty()) {
                ((Inspector)employees.get("I2")).pushButton(scanner.getOperationStation().getButtons()[2]);
                ((Inspector)employees.get("I2")).pushButton(scanner.getOperationStation().getButtons()[1]);
            }

            ((Inspector)employees.get("I2")).pushButton(scanner.getOperationStation().getButtons()[2]);
        }

        employees.get("T").enterPin(scanner.getOperationStation().getReader());
        ((Technician)employees.get("T")).executeMaintenance(scanner);

        ((Supervisor)employees.get("S")).switchPower(scanner);
        employees.get("S").enterPin(scanner.getOperationStation().getReader());
    }

    public Map<String, Employee> getEmployees() {
        return employees;
    }

    public BaggageScanner getScanner() {
        return scanner;
    }

    public static class Builder {
        private List<Passenger> passengers = new LinkedList<>();
        private Map<String, Employee> employeeMap = new HashMap<>();
        private Configuration configuration = new Configuration();

        public void defaultEmployees () {
            addEmployee(new Inspector("I1", "Clint Eastwood", "31.05.1930", true));
            addEmployee(new Inspector("I2", "Natalie Portman", "09.06.1981", false));
            addEmployee(new Inspector("I3", "Bruce Willis", "19.03.1955", true));
            addEmployee(new Supervisor("S", "Jodie Foster", "19.11.1962", false, false));
            addEmployee(new Technician("T", "Jason Statham", "26.07.1967"));
            addEmployee(new FederalPoliceOfficer("O1", "Wesley Snipes", "31.07.1962", "Officer"));
            addEmployee(new FederalPoliceOfficer("O2", "Toto", "01.01.1969", "Officer"));
            addEmployee(new FederalPoliceOfficer("O3", "Harry", "01.01.1969", "Officer"));
        }

        public void defaultPassengers() throws IOException, URISyntaxException {
            File passengerList = new File(getClass().getClassLoader().getResource("passenger_baggage.txt").toURI());
            BufferedReader reader = new BufferedReader(new FileReader(passengerList));
            String line = reader.readLine();
            while (line != null) {
                String[] passengerInformations = line.split(";");
                Passenger passenger = new Passenger(passengerInformations[0]);

                List<HandBaggage> baggages = new ArrayList<>(passengerInformations.length - 1);
                for (int i = 0; i < passengerInformations.length; i++) {
                    String fileName = "";
                    if (i < 10) {
                        fileName = "00" + Integer.toString(i);
                    } else if (i < 100) {
                        fileName = "0" + Integer.toString(i);
                    } else {
                        fileName = Integer.toString(i);
                    }
                    fileName += "_baggage.txt";
                    System.out.println(fileName);
                    File baggageFile = new File(getClass().getClassLoader().getResource(fileName).toURI());
                    BufferedReader baggageReader = new BufferedReader(new FileReader(baggageFile));
                    Layer[] layers = new Layer[5];
                    for (int j = 0; j < 5; j++) {
                        layers[j] = new Layer(baggageReader.readLine().toCharArray());
                    }
                    baggages.add(new HandBaggage(passenger, layers));
                }

                passenger.setBaggages(baggages.toArray(new HandBaggage[0]));
                addPassenger(passenger);
                line = reader.readLine();
            }
        }

        public void addPassenger(Passenger passenger) {
            passengers.add(passenger);
        }

        public void addEmployee(Employee employee) {
            employeeMap.put(employee.getId(), employee);
        }

        public Simulation build() {
            FederalPoliceOffice office = new FederalPoliceOffice();
            employeeMap.forEach((id, employee) -> {
               String pin = employee.getBirthDate().split("\\.")[2];
               String cardType;
               String profileType;

               switch(employee.getClass().getSimpleName()) {
                   case "Inspector":
                       profileType = "I";
                       cardType = "STAFF";
                       break;

                   case "Supervisor":
                       profileType = "S";
                       cardType = "STAFF";
                       break;

                   case "FederalPoliceOfficer":
                       profileType = "O";
                       cardType = "EXTERNAL";
                       break;

                   case "Technician":
                       profileType = "T";
                       cardType = "EXTERNAL";
                       break;

                   default:
                       cardType = "EXTERNAL";
                       profileType = "K";
               }

               String stripe = "***" + profileType + "***" + pin + "***";
               AES encryption = new AES(configuration.getKey());
               stripe = encryption.encrypt(stripe);
               IDCard idCard = new IDCard(employee.getId().hashCode(), Instant.ofEpochMilli(1607023620008L), stripe, cardType, false);
               employee.setIdCard(idCard);
            });

            employeeMap.values().stream().filter(employee -> employee instanceof FederalPoliceOfficer).
                    map(employee -> (FederalPoliceOfficer)employee).
                    forEach(officer -> {
                        office.getAllOfficers().add(officer);
                        officer.setOffice(office);
                    });
            office.getAllOfficers().sort(Comparator.comparing(Employee::getId));

            BaggageScanner baggageScanner = new BaggageScanner(configuration.getPermissions());
            Track[] tracks = new Track[]{new Track(1), new Track(2)};
            OperationStation operationStation = new OperationStation(baggageScanner);
            CardReader cardReader = new CardReader(new AES(configuration.getKey()), operationStation);

            Button[] buttons = new Button[3];
            buttons[0] = new Button(Button.BUTTON_LABEL_LEFT_ARROW, operationStation);
            buttons[1] = new Button(Button.BUTTON_LABEL_RECTANGLE, operationStation);
            buttons[2] = new Button(Button.BUTTON_LABEL_RIGHT_ARROW, operationStation);

            operationStation.setReader(cardReader);
            operationStation.setButtons(buttons);

            // Build the whole baggage scanner
            baggageScanner.setTraySupplier(new TraySupplyer(baggageScanner));
            baggageScanner.setTracks(tracks);
            baggageScanner.setManualPostControl(new ManualPostControl(baggageScanner, tracks[0], new TraceDetector()));
            baggageScanner.setOperationStation(operationStation);
            baggageScanner.setBelt(new Belt());
            baggageScanner.setRollerConveyor(new RollerConveyor(baggageScanner));
            baggageScanner.setScanner(new Scanner(configuration.getSearchAlgorithm()));
            baggageScanner.setSupervision(new Supervision(baggageScanner));

            return new Simulation(baggageScanner, passengers, employeeMap, office);
        }
    }
}
