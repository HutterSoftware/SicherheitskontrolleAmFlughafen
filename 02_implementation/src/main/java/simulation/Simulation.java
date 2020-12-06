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

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
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
            addEmployee(new HouseKeeping("K", "Jason Clarke", "17.07.169"));
        }
      
        public void defaultPassengers()  {
            try {
                char[] validCharacters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                        'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6',
                        '7', '8', '9', '!', '@', '#', '$', '^', '&', '*', '(', ')', '_', '-', '+', '=', '!'};

                char[][] plainContent = new char[5][10000];
                for (int j = 0; j < 5; j++) {
                    for (int i = 0; i < 10000; i++) {
                        plainContent[j][i] = validCharacters[(int)(System.currentTimeMillis()+i * 17) % validCharacters.length];
                        Thread.sleep(0,0);
                    }
                }


                File passengerList = new File(getClass().getClassLoader().getResource("passenger_baggage.txt").toURI());
                BufferedReader reader = new BufferedReader(new FileReader(passengerList));
                String line = reader.readLine();

                while (line != null) {


                    String[] passengerInformations = line.split(";");
                    int countOfBaggage = Integer.parseInt(passengerInformations[1]);
                    Passenger passenger = new Passenger(passengerInformations[0]);

                    List<String[]> listOfProhibitedItems = new ArrayList();
                    if (!passengerInformations[2].equals("-")) {
                        for (int i = 2; i < passengerInformations.length; i++) {
                            String[] parameterSplit = passengerInformations[i].replace("[","").
                                    replace("]","").split(",");

                            listOfProhibitedItems.add(parameterSplit);
                        }
                    }

                    int prohibitedItemCounter = 0;
                    HandBaggage[] baggages = new HandBaggage[countOfBaggage];



                    for (int i = 0; i < countOfBaggage; i++) {
                        Layer[] layerList = new Layer[5];

                        for (int j = 0; j < 5; j++) {
                            String layer = "";

                            if (prohibitedItemCounter < listOfProhibitedItems.size()) {
                                if ((Integer.parseInt(listOfProhibitedItems.get(prohibitedItemCounter)[1]) - 1) == i &&
                                    (Integer.parseInt(listOfProhibitedItems.get(prohibitedItemCounter)[2]) - 1) == j) {


                                    char[] modified = Arrays.copyOf(plainContent[j],plainContent[j].length);
                                    int itemLength = 0;
                                    String item = "";
                                    switch (listOfProhibitedItems.get(prohibitedItemCounter)[0]) {
                                        case "K":
                                            item = "kn!fe";
                                            itemLength = item.length();
                                            break;

                                        case "W":
                                            item = "glock|7";
                                            itemLength = item.length();
                                            break;

                                        case "E":
                                            item = "exp|os!ve";
                                            itemLength = item.length();
                                            break;
                                    }

                                    int startIndex = Math.abs((int)
                                            ((System.currentTimeMillis() * 17) % 10000) - itemLength);

                                    for (int k = startIndex; k < startIndex + itemLength; k++) {
                                        modified[k] = item.charAt(k - startIndex);
                                    }
                                    layer = new String(modified);
                                    prohibitedItemCounter++;
                                } else {
                                    layer = new String(plainContent[j]);
                                }
                            } else {
                                layer = new String(plainContent[j]);
                            }

                            layerList[j] = new Layer(layer.toCharArray());
                        }
                        baggages[i] = new HandBaggage(passenger, layerList);
                    }

                    passenger.setBaggages(baggages);
                    addPassenger(passenger);
                    line = reader.readLine();
                }
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            baggageScanner.setCurrentState(new State.Shutdown());
            Track[] tracks = new Track[]{new Track(1), new Track(2)};
            OperationStation operationStation = new OperationStation(baggageScanner);
            CardReader cardReader = new CardReader(new AES(configuration.getKey()), operationStation);

            Button[] buttons = new Button[3];
            buttons[0] = new Button(Button.BUTTON_LABEL_LEFT_ARROW, operationStation);
            buttons[1] = new Button(Button.BUTTON_LABEL_RECTANGLE, operationStation);
            buttons[2] = new Button(Button.BUTTON_LABEL_RIGHT_ARROW, operationStation);

            operationStation.setReader(cardReader);
            operationStation.setButtons(buttons);

            office.registerNewOfficer((FederalPoliceOfficer)employeeMap.get("O1"));
            office.registerNewOfficer((FederalPoliceOfficer)employeeMap.get("O2"));
            office.registerNewOfficer((FederalPoliceOfficer)employeeMap.get("O3"));

            // Build the whole baggage scanner
            baggageScanner.setTraySupplier(new TraySupplyer(baggageScanner));
            baggageScanner.setTracks(tracks);
            baggageScanner.setManualPostControl(new ManualPostControl(baggageScanner, tracks[0], new TraceDetector()));
            baggageScanner.getManualPostControl().setCurrentOfficer(new FederalPoliceOfficer[]{
                    (FederalPoliceOfficer)employeeMap.get("O1"),
                    (FederalPoliceOfficer)employeeMap.get("O2"),
                    (FederalPoliceOfficer)employeeMap.get("O3")});

            baggageScanner.setOperationStation(operationStation);
            baggageScanner.setBelt(new Belt());
            baggageScanner.setRollerConveyor(new RollerConveyor(baggageScanner));
            baggageScanner.setScanner(new Scanner(configuration.getSearchAlgorithm()));
            baggageScanner.setSupervision(new Supervision(baggageScanner));

            return new Simulation(baggageScanner, passengers, employeeMap, office);
        }

        public Configuration getConfiguration() {
            return configuration;
        }
    }
}
