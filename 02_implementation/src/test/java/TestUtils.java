import components.Scanner;
import components.Tray;
import passenger.HandBaggage;
import passenger.Layer;
import passenger.Passenger;
import simulation.Configuration;
import simulation.Simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

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
        HandBaggage baggage = new HandBaggage(passenger, layers);

        return baggage;
    }

    public static data.Record scanBaggage(HandBaggage baggage) {

        Configuration configuration = new Configuration();
        Scanner scanner = new Scanner(configuration.getSearchAlgorithm());

        Tray tray = new Tray();
        tray.insertBaggage(baggage);

        scanner.move(tray);

        data.Record record = scanner.scan();

        return record;
    }

    public static Simulation createSimulation() throws IOException, URISyntaxException {

        Simulation.Builder builder = new Simulation.Builder();

        builder.defaultEmployees();
//        builder.defaultPassengers();
        Simulation simulation = builder.build();
        simulation.initializeSimulation();

        return simulation;
    }
}
