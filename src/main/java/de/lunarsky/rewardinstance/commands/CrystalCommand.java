package de.lunarsky.rewardinstance.commands;

import de.lunarsky.rewardinstance.splinters.SplinterManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CrystalCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(sender instanceof Player p) {
            p.sendMessage("§7Du hast aktuell §6"+ SplinterManager.getAmount(p)+" Meteoritensplitter§7.");
            if(SplinterManager.getAmount(p) == 0) {
                p.sendMessage("§8> §7Meteoritensplitter kannst du durch Voten, Angeln oder in Kämpfen erhalten.");
            }
        }

        return false;
    }
}
