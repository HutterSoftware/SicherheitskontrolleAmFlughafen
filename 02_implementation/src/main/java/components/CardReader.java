package components;

import algorithms.AES;

public class CardReader {

    private OperationStation station;
    private AES aes;
    private int countWrongInputs;
    private IDCard card;

    public CardReader(AES encryption, OperationStation station) {
        this.aes = encryption;
        this.station = station;
    }

    public OperationStation getStation() {
        return station;
    }

    public void setStation(OperationStation station) {
        this.station = station;
    }

    public AES getAes() {
        return aes;
    }

    public void setAes(AES aes) {
        this.aes = aes;
    }

    public int getCountWrongInputs() {
        return countWrongInputs;
    }

    public void setCountWrongInputs(int countWrongInputs) {
        this.countWrongInputs = countWrongInputs;
    }

    public IDCard getCard() {
        return card;
    }

    public void setCard(IDCard card) {
        this.card = card;
    }

    public void swipeCard(IDCard card) {
        if (card.isLocked()) {
            System.out.println("IDCard is locked");
        }



        if (!this.card.equals(card)) {
            String stripeContent = aes.decrypt(card.getMagnetStripe());
            byte permissions = station.getScanner().getPermissions().get(stripeContent.split("\\*\\*\\*")[1]);
            if ((permissions & 1 << 7) != 0) {
                System.out.println("Card Reader: User type is not authorized");
            } else {
                System.out.println("Card Reader: Enter pin");
                countWrongInputs = 0;
                this.card = card;
            }
        }
    }

    public void enterPin(String pin) {
        String stripeContent = aes.decrypt(card.getMagnetStripe());

        if (pin.equals(stripeContent.split("\\*\\*\\*")[2])) {
            System.out.println("Card reader: Pin accepted");
            station.setAuthentication(stripeContent.split("\\*\\*\\*")[1]);
        } else {
            if (++countWrongInputs >= 3) {
                System.out.println("Card Reader: Card is locked");
                card.setLocked(true);
            } else {
                System.out.println("Card Reader: Please try again");
            }
        }
    }
}
