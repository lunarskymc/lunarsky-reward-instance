package de.lunarsky.rewardinstance.splinters;

import de.lunarsky.rewardinstance.Playerdata;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SplinterManager {
    public static final int max = 2;

    /**
     * sets the amount of splinters a player has, cant be more t han max (throw IllegalArgumentException=
     * @param p         player
     * @param amount    amount (cant be more than max)
     * @throws IllegalArgumentException thrown if more than max is set
     */
    public static void setSplinter(Player p, int amount) throws IllegalArgumentException {
        if(amount > max) throw new IllegalArgumentException("Cant add more than max!");

        FileConfiguration data = Playerdata.getPlayerData(p);
        data.set("splinter", amount);
        Playerdata.savePlayerdata(data, p.getUniqueId().toString());
    }

    /**
     * adds one splinter to the player if the ydont have enough
     * @param p     player
     * @throws IllegalArgumentException     if the player has already the max amount
     */
    public static void addSplinter(Player p) throws IllegalArgumentException {
        FileConfiguration data = Playerdata.getPlayerData(p);
        int amount = data.getInt("splinter");
        int newamount = amount + 1;
        if(newamount > max) throw new IllegalArgumentException("Cant add more than max! ("+max+")");
        data.set("splinter", newamount);
        Playerdata.savePlayerdata(data, p.getUniqueId().toString());
    }

    /**
     * sets the players splinter amount to 0
     * @param p     player
     */
    public static void clear(Player p) {
        FileConfiguration data = Playerdata.getPlayerData(p);
        data.set("splinter", 0);
        Playerdata.savePlayerdata(data, p.getUniqueId().toString());
    }

    /**
     * returns player amount of splinters
     * @param p     player
     * @return      amount
     */
    public static int getAmount(Player p) {
        return Playerdata.getPlayerData(p).getInt("splinter");
    }
}
