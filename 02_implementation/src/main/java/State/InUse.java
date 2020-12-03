package State;

public class InUse extends State {
    public State scanDone() {
        return new Activated();
    }
}
