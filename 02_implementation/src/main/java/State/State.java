package State;

public abstract class State {
    public State start() {
        return null;
    }

    public State shutdown() {
        return null;
    }

    public State authenticated() {
        return null;
    }

    public State scansDone() {
        return null;
    }

    public State lock() {
        return null;
    }

    public State unlock() {
        return null;
    }

    public State scan() {
        return null;
    }

    public State scanDone() {
        return null;
    }
}
