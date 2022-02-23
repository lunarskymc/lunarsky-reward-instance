package de.lunarsky.rewardinstance;

import de.lunarsky.rewardinstance.core.RewardInstancePlugin;
import de.lunarsky.rewardinstance.events.UseKeyItem;
import de.lunarsky.rewardinstance.splinters.SplinterManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class RotatingCrystal implements Listener {
    public static HashMap<Player, Long> timestamp = new HashMap<>();
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
            if(!e.getPlayer().getWorld().getName().equalsIgnoreCase("skyworld")) return;
            if(e.getClickedBlock().getLocation().distance(loc) <= 1.1) {
                Player p = e.getPlayer();
                if(timestamp.getOrDefault(p, 0L) + 3000 < System.currentTimeMillis()) {
                    if(SplinterManager.isOnCrystalCooldown(p)) {
                        TextComponent msg = new TextComponent("Es sieht ganz danach aus, als wäre der Amethyst erschöpft... Versuche es später noch ein Mal.");
                        msg.setColor(net.md_5.bungee.api.ChatColor.of("#d9abd4"));
                        p.spigot().sendMessage(msg);
                        p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
                        timestamp.put(p, System.currentTimeMillis());
                        return;
                    }
                    p.openInventory(UseKeyItem.getConfirmationInventory(p));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1, 1), 5l);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1, 3), 13l);
                }
            }
        }
    }
}
