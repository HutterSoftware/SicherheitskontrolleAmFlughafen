package State;

public class Deactivated extends State {
    public State shutdown() {
        return new Shutdown();
    }

    public State authenticated() {
        return new Activated();
    }
}
