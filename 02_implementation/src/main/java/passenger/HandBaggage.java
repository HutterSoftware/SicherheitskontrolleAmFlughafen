package passenger;

import explosivedevicecomponents.TestStrip;
import test.write;

import java.util.Arrays;

public class HandBaggage {

    private Passenger owner;
    private Layer[] layers;
    private boolean testFlag = false;

    public HandBaggage(Passenger passenger, Layer[] layers) {
        this.owner = passenger;
        this.layers = layers;
    }

    public Layer[] getLayers() {
        return layers;
    }

    public TestStrip swipeTest() {
        if (testFlag) new write().writeTestFile("swipeTest");
        return new TestStrip();
    }

    public String takeContent(int layer, int position, int length) {
        if (testFlag) new write().writeTestFile("removeItem");
        char[] origin = layers[layer].getContent();
        char[] copy = Arrays.copyOfRange(origin, position, position + length);
        for (int i = 0; i < length; i++) {
            origin[position + i] = ' ';
        }

        layers[layer].setContent(origin);
        String copyString = new String(copy);
        return copyString;
    }

    public Passenger getOwner() {
        return owner;
    }

    public void clearLayer() {
        this.layers = null;
    }

    public void setTestFlag(boolean testFlag) {
        this.testFlag = testFlag;
    }
}
