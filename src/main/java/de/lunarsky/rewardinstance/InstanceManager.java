package de.lunarsky.rewardinstance;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class InstanceManager {
    public static final String worldname = "instances";
    private static ArrayList<Instance> instances = new ArrayList<Instance>();

    public static Instance registerNewInstance(Player p) {
        Instance instance = new Instance(p, Helper.randInt(1, 10000));
        instance.loadSchematic();
        instances.add(instance);
        return instance;
    }

    public static void removeInstance(int id) {
        instances.removeIf(i -> i.getId() == id);
    }

    public static void checkInstanceWorld() {
        // todo: check if world exist / create / recreate empty voidgen
    }

    /**
     * returns the instance of the player or null if they dont have one
     * @param p     player
     * @return      instance / null
     */
    public static Instance getInstanceOfPlayer(Player p) {
        for(Instance i : instances) {
            if(i.getP().equals(p)) return i;
        }
        return null;
    }
    /**
     * returns the instance with that id or null if it doesnt exist
     * @param id    id
     * @return      instance / null
     */
    public static Instance getInstanceById(int id) {
        for(Instance i : instances) {
            if(i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    /**
     * removes all holograms from all instances (used on server stop)
     */
    public static void removeHolograms() {
        instances.forEach(i -> i.getHologram().delete());
    }
}
