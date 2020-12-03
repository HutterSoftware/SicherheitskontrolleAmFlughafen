package staff;

import components.CardReader;
import components.IDCard;

public class Employee {
    protected String id;
    protected String name;
    protected String birthDate;
    protected IDCard idCard;

    public void enterPin(CardReader reader) {

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
}
