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

        String stripeContent = aes.decrypt(card.getMagnetStripe());
        byte permissions = station.getScanner().getPermissions().get(stripeContent.split("\\*\\*\\*")[1]);
        if ((permissions & 1 << 7) != 0) {

        }
    }

    public void enterPin(String pin) {

    }
}
