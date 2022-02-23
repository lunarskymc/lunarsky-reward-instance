package de.lunarsky.rewardinstance.splinters;

import de.lunarsky.rewardinstance.Helper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GuessSplinterCommand implements CommandExecutor {
    private static final String SYNTAX = "§c/guesssplinter <player>";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender.hasPermission("lunarsky.instance.guesssplinter")) {
            if(args.length != 1) {
                sender.sendMessage(SYNTAX);
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage("§cSpieler nicht gefunden!");
                return false;
            }

            if(Helper.randInt(0, 100) < 9) {
                try {
                    SplinterManager.addSplinter(target);
                    target.sendMessage("§6+ Meteoritensplitter");
                }catch (IllegalArgumentException e) {
                    sender.sendMessage("§cSplitter konnten nicht hinzugefügt werden, da der Spieler bereits zu viele Splitter besitzt! (2)");
                    target.sendMessage("§7Du kannst nicht noch mehr §6Meteoritensplitter§7 tragen. (2)");
                    target.sendMessage("§7Der Splitter ist dir heruntergefallen und hat sich aufgelöst.");
                }
            }
            else {
                target.sendMessage("§7Du hast leider keinen Splitter erhalten. Viel Glück beim nächsten mal :)");
            }


        }
        return false;
    }
}
