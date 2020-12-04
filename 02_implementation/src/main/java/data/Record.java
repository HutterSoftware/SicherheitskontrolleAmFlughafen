package data;

public class Record {

    private int id;
    private String timestamp;
    private ScanResult result;

    public Record(int i, String timestamp, ScanResult scanResult) {
        this.id = id;
        this.timestamp = timestamp;
        this.result = scanResult;
    }

    public int getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public ScanResult getResult() {
        return result;
    }
}
