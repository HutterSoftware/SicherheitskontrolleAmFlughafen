package components;

import staff.Employee;

public class OperationStation {

    private BaggageScanner scanner;
    private String authentication;
    private Employee employee;
    private CardReader reader;
    private Button[] buttons;
    private String userType;

    public OperationStation(BaggageScanner scanner) {
        this.scanner = scanner;
    }

    public BaggageScanner getScanner() {
        return scanner;
    }

    public void setScanner(BaggageScanner scanner) {
        this.scanner = scanner;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public CardReader getReader() {
        return reader;
    }

    public void setReader(CardReader reader) {
        this.reader = reader;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public void setButtons(Button[] buttons) {
        this.buttons = buttons;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
