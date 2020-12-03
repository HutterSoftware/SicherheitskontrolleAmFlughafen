package components;

public class Button {

    public static String BUTTON_LABEL_RIGHT_ARROW = "RIGHT_ARROW";
    public static String BUTTON_LABEL_LEFT_ARROW = "LEFT_ARROW";
    public static String BUTTON_LABEL_RECTANGLE = "RECTANGLE";

    private String buttonLabel;
    private OperationStation station;

    public Button(String buttonLabel, OperationStation operationStation) {
        this.station = operationStation;
        this.buttonLabel = buttonLabel;
    }

    public void push() {
        switch(buttonLabel) {
            case "RIGHT_ARROW":
                station.getScanner().moveBeltForward();
                break;

            case "LEFT_ARROW":
                station.getScanner().moveBeltBackwards();
                break;

            case "RECTANGLE":
                station.getScanner().scanHandBaggage();
                break;
        }
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public OperationStation getStation() {
        return station;
    }
}
