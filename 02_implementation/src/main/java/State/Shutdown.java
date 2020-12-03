package State;

public class Shutdown extends State{
    public State start() {
        return new Deactivated();
    }
}
