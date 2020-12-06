package explosivedevicecomponents;

import algorithms.BruteForce;
import algorithms.IStringMatching;
import test.write;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TraceDetector {

    IStringMatching matcher = new BruteForce();
    private boolean testFlag = false;

    public boolean testStripe(TestStrip test) {
        if (testFlag) new write().writeTestFile("TraceDetectorTestStripe");
        System.out.println("Testing stripe");
        String string = Arrays.stream(test.getTestStripe()).map(String::new).collect(Collectors.joining());
        return matcher.search(string, "exp") != -1;
    }

    public void setTestFlag(boolean testFlag) {
        this.testFlag = testFlag;
    }
}
