package components;

import passenger.HandBaggage;

public interface IBaggageScanner {
    public void scanHandBaggage();
    public void moveBeltForward();
    public void moveBeltBackwards();
    public void alarm();
    public void report();
    public void maintenance();
}
