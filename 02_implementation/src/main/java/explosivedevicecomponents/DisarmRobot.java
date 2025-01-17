package explosivedevicecomponents;

import passenger.HandBaggage;
import passenger.Layer;
import test.write;

import java.util.Arrays;

public class DisarmRobot {

    private boolean testFlag = false;

    public void destroyBaggage(HandBaggage baggage) {
        Arrays.asList(baggage.getLayers()).stream().map(Layer::getContent).map(chars -> {
           char[][] returnContent = new char[200][50];
           for (int i = 0; i < 200; i++) {
               returnContent[i] = Arrays.copyOfRange(chars, 50*i, 50*(i+1));
           }
           return returnContent;
        }).forEach(x -> Arrays.stream(x).map(String::new).forEach(string -> System.out.println("Elements: " + string)));

        baggage.clearLayer();
        System.out.println("Baggage was destroyed");
        if (testFlag) new write().writeTestFile("robotDestroyedBaggage");
    }

    public void setTestFlag(boolean testFlag) {
        this.testFlag = testFlag;
    }
}
