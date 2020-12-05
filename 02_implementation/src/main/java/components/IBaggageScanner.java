package components;

import passenger.HandBaggage;

public interface IBaggageScanner {
    public boolean scanHandBaggage();
    public boolean moveBeltForward();
    public boolean moveBeltBackwards();
    public boolean alarm();
    public boolean report();
    public boolean maintenance();
}
