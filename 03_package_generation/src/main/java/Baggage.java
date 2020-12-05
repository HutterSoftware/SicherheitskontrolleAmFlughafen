import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Baggage {

    private int id;
    private List<String> baggageContent = new ArrayList<>();

    public Baggage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addLayer(String layer) {
        baggageContent.add(layer);
    }

    public void toFile(String prefix) {
        String fileName = prefix;
        if (id < 10) {
            fileName = "00";
        } else if (id < 100) {
            fileName = "0";
        }
        fileName += Integer.toString(id) + "_baggage.txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            for (String layer : baggageContent) {
                writer.write(layer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
