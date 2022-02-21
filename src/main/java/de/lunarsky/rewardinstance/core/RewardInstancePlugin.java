package de.lunarsky.rewardinstance.core;

import de.lunarsky.rewardinstance.InstanceLoot;
import de.lunarsky.rewardinstance.InstanceManager;
import de.lunarsky.rewardinstance.PlaceholderInstance;
import de.lunarsky.rewardinstance.commands.CrystalCommand;
import de.lunarsky.rewardinstance.commands.GiveRewardItemCommand;
import de.lunarsky.rewardinstance.events.InstanceEvents;
import de.lunarsky.rewardinstance.events.UseKeyItem;
import de.lunarsky.rewardinstance.splinters.AddSplinterCommand;
import de.lunarsky.rewardinstance.splinters.GuessSplinterCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RewardInstancePlugin extends JavaPlugin {
    private static RewardInstancePlugin instance = null;
    @Override
    public void onEnable() {
        registerCommands();
        registerListeners();
        instance = this;
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, InstanceManager::checkInstanceWorld, 20L);
        InstanceLoot.fillLoot();
        new PlaceholderInstance().register();
    }

    @Override
    public void onDisable() {
        InstanceManager.removeHolograms();
    }

    private void registerCommands() {
        getCommand("givekey").setExecutor(new GiveRewardItemCommand());
        getCommand("addsplinter").setExecutor(new AddSplinterCommand());
        getCommand("peekloot").setExecutor(new InstanceLoot());
        getCommand("guesssplinter").setExecutor(new GuessSplinterCommand());
        getCommand("crystals").setExecutor(new CrystalCommand());
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new UseKeyItem(), this);
        pm.registerEvents(new InstanceEvents(), this);
    }

    public static RewardInstancePlugin getInstance() {
        return instance;
    }
}
