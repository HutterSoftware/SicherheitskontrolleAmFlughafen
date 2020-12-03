package State;

public class Locked extends State {
    public State unlock() {
        return new Activated();
    }
}
