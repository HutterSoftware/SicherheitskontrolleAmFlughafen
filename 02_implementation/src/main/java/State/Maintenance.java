package State;

public class Maintenance extends State {

    public State shutdown() {
        return new Shutdown();
    }
}
