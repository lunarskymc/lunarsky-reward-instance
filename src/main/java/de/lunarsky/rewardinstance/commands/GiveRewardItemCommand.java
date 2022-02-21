package de.lunarsky.rewardinstance.commands;

import de.lunarsky.rewardinstance.Helper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GiveRewardItemCommand implements CommandExecutor {
    private static final String SYNTAX = "§c/givekey <player>";
    private static final ItemStack keyItem = Helper.createSimpleIcon(Material.AMETHYST_SHARD, "§bSchillernder Amethyst", new String[]{"§7Dieser Kristall scheint", "§7ein Tor öffnen zu können...", "§8???"});
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender.hasPermission("lunarsky.instance.givekeycommand")) {
            if(args.length != 1) {
                sender.sendMessage(SYNTAX);
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage("§cSpieler nicht gefunden!");
                return false;
            }

            ItemStack key = getKey();
            target.getInventory().addItem(key);
        }
        return false;
    }

    /**
     *
     * @return the key item that is used to enter dungeons
     */
    public static ItemStack getKey() {
        return keyItem;
    }
}
