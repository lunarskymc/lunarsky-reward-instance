package de.lunarsky.rewardinstance;

import de.lunarsky.rewardinstance.splinters.SplinterManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderInstance extends PlaceholderExpansion {
    public String onPlaceholderRequest(Player player, String identifier){
        Instance instance = InstanceManager.getInstanceOfPlayer(player);
        if(instance != null) {
            if(identifier.equalsIgnoreCase("remaining-enemies")) {
                return instance.getRemainingEnemiesCount() + "";
            }

            if(identifier.equalsIgnoreCase("killed-enemies")) {
                return instance.getEnemies().stream().filter(Entity::isDead).count() + "";
            }

            if(identifier.equalsIgnoreCase("enemy-amount")) {
                return instance.getEnemies().size() + "";
            }
            if(identifier.equalsIgnoreCase("stage")) {
                return instance.getStage() + "";
            }
        }

        if(identifier.equalsIgnoreCase("splinter")) {
            return SplinterManager.getAmount(player) + "";
        }

        if(identifier.equalsIgnoreCase("splinter-max")) {
            return SplinterManager.max + "";
        }



        return "§cx";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "instance";
    }

    @Override
    public @NotNull String getAuthor() {
        return "treppi";
    }

    @Override
    public @NotNull String getVersion() {
        return "x";
    }
}
