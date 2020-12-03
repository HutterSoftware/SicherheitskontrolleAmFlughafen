package components;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public class Belt {

    private Deque<Tray> trayQueue = new LinkedBlockingDeque<>();

    public Tray moveForward() {
        return trayQueue.pollFirst();
    }

    public void moveBackwards(Tray fromScanner) {
        trayQueue.addFirst(fromScanner);
    }

    public void queueTray(Tray tray) {
        this.trayQueue.addLast(tray);
    }

    public Deque<Tray> getTrayQueue() {
        return this.trayQueue;
    }
}
