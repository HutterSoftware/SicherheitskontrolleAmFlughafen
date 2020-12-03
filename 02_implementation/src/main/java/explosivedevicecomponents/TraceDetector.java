package explosivedevicecomponents;

import algorithms.BruteForce;
import algorithms.IStringMatching;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TraceDetector {

    IStringMatching matcher = new BruteForce();

    public boolean testStripe(TestStrip test) {
        System.out.println("Testing stripe");
        String string = Arrays.stream(test.getTestStripe()).map(String::new).collect(Collectors.joining());
        return matcher.search(string, "exp") != -1;
    }
}
