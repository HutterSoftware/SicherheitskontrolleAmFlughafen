package explosivedevicecomponents;

import java.util.Arrays;
import java.util.Random;

public class TestStrip {

    private char[][] testStripe;

    public TestStrip() {
        testStripe = new char[30][10];

        for (char[] c : testStripe) {
            Arrays.fill(c, 0, c.length, 'p');
        }

        Random rng = new Random();
        int row = rng.nextInt(testStripe.length);
        int column = rng.nextInt(testStripe[row].length - 2);

        testStripe[row][column] = 'e';
        testStripe[row][column + 1] = 'x';
        testStripe[row][column + 2] = 'p';
    }

    public char[][] getTestStripe() {
        return testStripe;
    }
}
