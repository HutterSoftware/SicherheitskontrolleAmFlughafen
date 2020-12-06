package components;

import data.Record;
import data.ScanResult;
import passenger.HandBaggage;
import algorithms.IStringMatching;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Scanner {
    private Tray currentTray;
    private int scanCount;
    private IStringMatching stringMatcher;

    public Scanner(IStringMatching searchAlgorithm) {
        this.stringMatcher = searchAlgorithm;
    }

    public Tray move(Tray tray) {
        Tray t = currentTray;
        currentTray = tray;
        return t;
    }

    public Tray getCurrentTray() {
        return currentTray;
    }

    public data.Record scan() {
        if (currentTray == null || currentTray.getContainedBaggage() == null) {
            throw new RuntimeException("Tray does not contain hand baggage");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss,SSS");
        HandBaggage baggage = currentTray.getContainedBaggage();

        String[] prohibitedItems = {"kn!fe", "glock|7", "exp|os!ve"};

        for (int i = 0; i < baggage.getLayers().length; i++) {
            for (int j = 0; j < prohibitedItems.length; j++) {
                String source = new String(baggage.getLayers()[i].getContent());
                int result = stringMatcher.search(source, prohibitedItems[j]);

                if (result != -1) {
                    ScanResult scanResult =new ScanResult(ScanResult.ITEM_TYPE_PROHIBITED_ITEM,
                            prohibitedItems[j],
                            new int[]{i,result});

                    return new Record(scanCount++, formatter.format(LocalDateTime.now()), scanResult);
                }
            }
        }

        return new data.Record(scanCount++,
                formatter.format(LocalDateTime.now()),
                new ScanResult("CLEAN", null, null)
                );
    }
}
