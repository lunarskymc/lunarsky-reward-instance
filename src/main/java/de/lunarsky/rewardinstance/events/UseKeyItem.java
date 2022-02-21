package de.lunarsky.rewardinstance.events;

import de.lunarsky.rewardinstance.Helper;
import de.lunarsky.rewardinstance.Instance;
import de.lunarsky.rewardinstance.InstanceManager;
import de.lunarsky.rewardinstance.commands.GiveRewardItemCommand;
import de.lunarsky.rewardinstance.core.RewardInstancePlugin;
import de.lunarsky.rewardinstance.splinters.SplinterManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UseKeyItem implements Listener {
    private static final String inventoryTitle = "§5§lSplitter einsetzen?";
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(!e.hasItem()) return;

        if(e.getItem().equals(GiveRewardItemCommand.getKey())) {
            Player p = e.getPlayer();
            p.openInventory(getConfirmationInventory(p));
            Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1, 1), 5l);
            Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1, 3), 13l);
        }
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        if(!e.getView().getTitle().equals(inventoryTitle)) return;
        e.setCancelled(true);
        if(e.getCurrentItem() != null) {
            if(e.getCurrentItem().equals(GiveRewardItemCommand.getKey())) {
                Player p = (Player) e.getWhoClicked();
                if(SplinterManager.getAmount(p) >= 2) {
                    Instance instance = InstanceManager.registerNewInstance(p);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> {
                        p.closeInventory();
                        p.sendMessage("§7Du hast die Splitter in den Amethyst eingesetzt!");
                        Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> {
                            p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 10);
                        }, 15L * 3);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> {
                            p.teleport(instance.getSpawn());
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                            p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 10);
                            SplinterManager.clear(p);
                        }, 20L * 3);

                    }, 10L);
                }
                else {
                    p.sendMessage("§7Du hast nicht genügend Splitter bei dir!");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                }
            }
        }
    }

    /**
     * create confirmation inventory
     * @param p     player because it checks data
     * @return      built inventory
     */
    private Inventory getConfirmationInventory(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9*3, inventoryTitle);
        int playerSplinterAmount = SplinterManager.getAmount(p);
        ItemStack gray = Helper.createSimpleIcon(Material.GRAY_STAINED_GLASS_PANE, "§0", new String[]{});
        ItemStack purple = Helper.createSimpleIcon(Material.PURPLE_STAINED_GLASS_PANE, "§0", new String[]{});
        ItemStack magenta = Helper.createSimpleIcon(Material.MAGENTA_STAINED_GLASS_PANE, "§0", new String[]{});
        ItemStack pink = Helper.createSimpleIcon(Material.PINK_STAINED_GLASS_PANE, "§0", new String[]{});

        for(int i : new int[]{0, 1, 2, 9, 18}) inv.setItem(i, gray);
        for(int i : new int[]{3, 11, 12, 13, 19, 20}) inv.setItem(i, purple);
        for(int i : new int[]{4, 5, 6, 21, 22, 23}) inv.setItem(i, magenta);
        for(int i : new int[]{7, 8, 17, 24, 25, 26}) inv.setItem(i, pink);

        inv.setItem(10, Helper.createSimpleIcon(Material.MOJANG_BANNER_PATTERN, "§r§b§nSplitter einsetzen", new String[]{
                "§7Klicke auf den Amethyst,",
                "§7um die §cMeteoritensplitter",
                "§7einzusetzen.",
                "",
                "§7Du hast gerade §6" + playerSplinterAmount + " / " + SplinterManager.max + "",
                "§6Meteoritensplitter§7."
        }));

        inv.setItem(14, GiveRewardItemCommand.getKey());

        ItemStack nonexistantSplinter = Helper.createSimpleIcon(Material.FLINT, "§c❌ Meteoritensplitter", new String[]{
                "§7Diesen Splitter hast",
                "§7du noch nicht gesammelt.",
                "§7Du kannst Splitter durch",
                "§cVoten§7, §bAngeln §7oder",
                "§7in §cKämpfen §7erhalten."
        });

        ItemStack splinter = Helper.createSimpleIcon(Material.FLINT, "§3✔ §cMeteoritensplitter", new String[]{
                "§7Du hast den Splitter",
                "§7bei dir und kannst ihn",
                "§7benutzen."
        });

        inv.setItem(15, playerSplinterAmount >= 1 ? splinter : nonexistantSplinter);
        inv.setItem(16, playerSplinterAmount >= 2 ? splinter : nonexistantSplinter);

        return inv;
    }
}
