import components.Scanner;
import components.Tray;
import data.Record;
import data.ScanResult;
import org.junit.Test;
import passenger.HandBaggage;
import passenger.Layer;
import passenger.Passenger;
import simulation.Configuration;

import java.io.*;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class TestSecurity {

    @Test
    public void simulationTest() {

    }

    @Test
    public void employeePositionTest() {

    }

    @Test
    public void cancelIDCardTest() {

    }

    @Test
    public void scannerLoginTest() {

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

        data.Record record = TestUtils.scanBaggage(baggage);
        ScanResult result = record.getResult();

        assertEquals("PROHIBITED", result.getItemType());
        assertEquals("kn!fe", result.getProhibitedItemType());
    }

    @Test
    public void weaponTest() {

    }

    @Test
    public void explosivesTest() {

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
