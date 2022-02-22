package de.lunarsky.rewardinstance;

import org.bukkit.Location;

public class CircularDamageInfo {
    private Location loc;
    private int instanceID;
    private int size;
    private String id;

    public CircularDamageInfo(String id, Location loc, int size, int instanceID) {
        this.id = id;
        this.loc = loc;
        this.size = size;
        this.instanceID = instanceID;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public int getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(int instanceID) {
        this.instanceID = instanceID;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
