import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class PackageGeneration {

    private static int idCounter = 1;

    public static void incrementId() {
        idCounter++;
    }

    public static int getId() {
        return idCounter;
    }

    public static void main(String[] args)  {
        try {
            File passengerBaggageFile = new File("./src/main/resources/passenger_baggage.txt");
            if (!passengerBaggageFile.exists()) {
                throw new Exception("File not found");
            }
int packageCounter = 0;
            BufferedReader stream = new BufferedReader(new FileReader(passengerBaggageFile.getPath()));
            String passengerLine = stream.readLine();
            List<Baggage> baggageList = new ArrayList<>();

            while (passengerLine != null) {
                Passenger passenger = new Passenger(passengerLine);
                if (idCounter == 4) {
                    System.out.println("break");
                }
                passenger.createBaggage(passenger.getName(), baggageList);

                packageCounter += passenger.getCountOfPackages();
                passengerLine = stream.readLine();
            }
            System.out.println(packageCounter);
            System.out.println(baggageList.size());
            for (Baggage baggage : baggageList) {
                baggage.toFile("./");
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
