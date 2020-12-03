package simulation;

import algorithms.IStringMatching;
import algorithms.KnuthMorrisPratt;

import java.util.HashMap;

public class Configuration {
    private HashMap<String, Byte> permissions;
    private IStringMatching matcher;
    private String key;

    public Configuration() {
        // Permissions of different user types
        permissions.put("K", (byte) 0b00001000);
        permissions.put("O", (byte) 0b00001000);
        permissions.put("T", (byte) 0b00000100);
        permissions.put("S", (byte) 0b00000010);
        permissions.put("I", (byte) 0b00000001);

        key = "we2#$@a4o;";
        matcher = (IStringMatching) new KnuthMorrisPratt();
    }

    public HashMap<String, Byte> getPermissions() {
        return this.permissions;
    }

    public IStringMatching getMatcher() {
        return this.matcher;
    }

    public String getKey() {
        return this.key;
    }

    public IStringMatching getSearchAlgorithm () {
        return matcher;
    }
}
