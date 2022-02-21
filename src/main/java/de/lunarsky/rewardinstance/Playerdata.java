package de.lunarsky.rewardinstance;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class Playerdata {
    public static FileConfiguration getPlayerData(String uuid) {
        return YamlConfiguration.loadConfiguration(new File("plugins/lunarsky/playerdata/"+uuid));
    }

    public static FileConfiguration getPlayerData(Player player) {
        String uuid = player.getUniqueId().toString();
        return getPlayerData(uuid);
    }

    public static void savePlayerdata(FileConfiguration data, String uuid) {
        try {
            data.save(new File("plugins/lunarsky/playerdata/"+uuid));
        }catch (Exception ignored) {

        };
    }
}
