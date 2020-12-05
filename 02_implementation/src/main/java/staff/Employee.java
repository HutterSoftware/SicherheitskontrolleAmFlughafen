package staff;

import components.CardReader;
import components.IDCard;

public class Employee {
    protected String id;
    protected String name;
    protected String birthDate;
    protected IDCard idCard;

    public void enterPin(CardReader reader) {
        System.out.println("Employee: Type pin");
        reader.swipeCard(idCard);
        reader.enterPin(birthDate.split("\\.")[2]);
    }

    public void enterPin(CardReader reader, String pin) {
        System.out.println("Employee: Type pin");
        reader.swipeCard(idCard);
        reader.enterPin(pin);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public IDCard getIdCard() {
        return this.idCard;
    }

    public void setIdCard(IDCard idCard) {
        this.idCard = idCard;
    }
}
