package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class write {

    public void writeTestFile(String line) {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("./src/main/resources/Procedure.txt", true));
            writer.append(line);
            writer.append("|");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
