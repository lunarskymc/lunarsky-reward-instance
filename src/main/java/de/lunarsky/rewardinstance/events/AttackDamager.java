package de.lunarsky.rewardinstance.events;

import de.lunarsky.rewardinstance.CircularDamageInfo;
import de.lunarsky.rewardinstance.core.RewardInstancePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class AttackDamager implements Listener {
    private static ArrayList<CircularDamageInfo> infos = new ArrayList<>();
    private static int ticks = 0;
    public static void addInfo(CircularDamageInfo info) {
        infos.add(info);
    }
    public static void removeInfo(String uuid) {
        infos.removeIf(i -> i.getId().equalsIgnoreCase(uuid));
    }

    public static void startTicking() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(RewardInstancePlugin.getInstance(), () -> {
            ticks++;

            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getWorld().getName().equalsIgnoreCase("instances"))
                    .forEach(p -> {
                        for(CircularDamageInfo info : infos) {
                            if(p.getLocation().distance(info.getLoc()) < info.getSize()) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 10, 2, false));
                                p.setHealth(Math.max(0, p.getHealth() - 2.5));
                            }
                        }
                    });

        }, 10L, 10L);
    }
}
