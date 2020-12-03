package State;

public class Activated extends State {
    public State scansDone() {
        return new Maintenance();
    }

    public State lock() {
        return new Locked();
    }

    public State scan() {
        return new InUse();
    }
}
