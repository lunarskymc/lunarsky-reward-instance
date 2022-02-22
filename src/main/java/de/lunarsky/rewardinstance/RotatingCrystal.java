package de.lunarsky.rewardinstance;

import de.lunarsky.rewardinstance.core.RewardInstancePlugin;
import de.lunarsky.rewardinstance.events.UseKeyItem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class RotatingCrystal implements Listener {
    static World w;
    static Location loc;
    private static int angel;

    public static void spawn() {
        w = Bukkit.getWorld("skyworld");
        loc = new Location(w, 5.5, 148, -25.5);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(RewardInstancePlugin.getInstance(), () -> {
            angel += 1;
            if(angel > 360) angel = 0;
            Bukkit.getOnlinePlayers()
                    .stream()
                    .filter(p -> p.getWorld() == loc.getWorld())
                    .filter(p -> p.getLocation().distance(loc) < 200)
                    .forEach(p -> Helper.displayParticleTwirl(p, loc, angel, Color.fromRGB(196, 138, 227)));
        }, 20L, 5L);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(e.hasBlock()) {
            if(e.getClickedBlock().getLocation().distance(loc) <= 1.1) {
                Player p = e.getPlayer();
                p.openInventory(UseKeyItem.getConfirmationInventory(p));
                Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1, 1), 5l);
                Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1, 3), 13l);
            }
        }
    }
}
