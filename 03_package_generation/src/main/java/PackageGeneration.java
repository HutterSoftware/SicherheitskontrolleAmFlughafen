import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.Buffer;

public class PackageGeneration {
    public static void main(String[] args) throws Exception {
        File passengerBaggageFile = new File("./src/main/resources/passenger_baggage.txt");
        if (!passengerBaggageFile.exists()) {
            throw new Exception("File not found");
        }

        BufferedReader stream = new BufferedReader(new FileReader(passengerBaggageFile.getPath()));
        String passengerLine = stream.readLine();
        int counter = 1;
        while (passengerLine != null) {
            Passenger passenger = new Passenger(passengerLine);
            passenger.toFile(counter);

            passengerLine = stream.readLine();
            counter++;
        }
    }
}
