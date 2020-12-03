package components;

import staff.Employee;

import java.time.Instant;

public class IDCard {

    public static String TYPE_STAFF = "STAFF";
    public static String TYPE_EXTERNAL = "EXTERNAL";

    private int id;
    private Employee owner;
    private boolean isLocked;
    private String type;
    private Instant expirationTime;
    private String magnetStripe;

    public IDCard(int id, Instant expirationTime, String magnetStripe, String type, boolean isLocked) {
        this.id = id;
        this.expirationTime = expirationTime;
        this.magnetStripe = magnetStripe;
        this.type = type;
        this.isLocked = isLocked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getMagnetStripe() {
        return magnetStripe;
    }

    public void setMagnetStripe(String magnetStripe) {
        this.magnetStripe = magnetStripe;
    }
}
