package components;

import passenger.HandBaggage;

public class Tray {
    private HandBaggage baggage;

    public Tray() {

    }

    public Tray(HandBaggage baggage) {
        this.baggage = baggage;
    }

    public void insertBaggage(HandBaggage baggage) {
        if (baggage == null) {
            throw new RuntimeException("Baggage is empty");
        }
        this.baggage = baggage;
    }

    public HandBaggage takeBaggage() {
        HandBaggage t = this.baggage;
        this.baggage = null;
        return t;
    }

    public HandBaggage getContainedBaggage() {
        return baggage;
    }

    public void putBaggage(HandBaggage baggage) {
        if (this.baggage == null) {
            this.baggage = baggage;
        }
    }
}
