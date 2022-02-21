package de.lunarsky.rewardinstance.events;

import de.lunarsky.rewardinstance.Helper;
import de.lunarsky.rewardinstance.Instance;
import de.lunarsky.rewardinstance.InstanceLoot;
import de.lunarsky.rewardinstance.InstanceManager;
import de.lunarsky.rewardinstance.commands.GiveRewardItemCommand;
import de.lunarsky.rewardinstance.core.RewardInstancePlugin;
import de.lunarsky.rewardinstance.splinters.SplinterManager;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class InstanceEvents implements Listener {
    // cancel all block interactions in own instance,
    // also check if loot block was clicked
    @EventHandler
    public void onLootClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(p.getWorld().getName().equalsIgnoreCase(InstanceManager.worldname)) {
            Instance instance = InstanceManager.getInstanceOfPlayer(p);
            if(instance != null) {
                e.setCancelled(true);
                Block b = e.getClickedBlock();
                if(b.equals(instance.getLootBlock())) {
                    p.openInventory(instance.getLootInventory());
                    World w = b.getWorld();
                    w.spawnParticle(Particle.END_ROD, b.getLocation(), 30);
                }
            }
        }
    }

    @EventHandler
    public void onFinishedLooting(InventoryCloseEvent e) {
        if(e.getView().getTitle().equals(Instance.lootInventoryName)) {
            Player p = (Player) e.getPlayer();
            Instance instance = InstanceManager.getInstanceOfPlayer(p);
            if(instance != null) {
                instance.spawnMobs(p);
            }
        }
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        if(entity.getWorld().getName().equalsIgnoreCase(InstanceManager.worldname)) {
            e.getDrops().clear();
            World world = entity.getWorld();
            world.dropItemNaturally(entity.getLocation(), InstanceLoot.getRandomItem());
            world.spawnParticle(Particle.SPELL_WITCH, entity.getLocation(), 3);

            double x = e.getEntity().getLocation().getX();
            int id = (int) (x / 3000);
            Instance instance = InstanceManager.getInstanceById(id);
            if(instance != null) {
                instance.getP().sendMessage("§7"+instance.getRemainingEnemiesCount() + " von " + instance.getEnemiesCount() + " Gegnern verbleibend.");
            }
        }
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        Entity entity = e.getEntity();
        if(entity.getWorld().getName().equalsIgnoreCase(InstanceManager.worldname)) {
            if(e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent e) {
        if(e.getPlayer().getWorld().getName().equals(InstanceManager.worldname)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if(e.getPlayer().getWorld().getName().equals(InstanceManager.worldname)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if(e.getEntity().getWorld().getName().equalsIgnoreCase(InstanceManager.worldname)) {
            Instance instance = InstanceManager.getInstanceOfPlayer(e.getEntity());
            if(instance != null) {
                instance.onFail();
            }
        }
    }

    @EventHandler
    public void onDropout(PlayerMoveEvent e) {
        if(e.getPlayer().getWorld().getName().equalsIgnoreCase(InstanceManager.worldname)) {
            if(e.getPlayer().getLocation().getY() < 120) {
                Instance instance = InstanceManager.getInstanceOfPlayer(e.getPlayer());
                if(instance != null) {
                    instance.onFail();
                }
            }
        }
    }

    @EventHandler
    public void onUsePortal(PlayerMoveEvent e) {
        Player p = (Player) e.getPlayer();
        if(p.getWorld().getName().equalsIgnoreCase(InstanceManager.worldname)) {
            Instance instance = InstanceManager.getInstanceOfPlayer(p);
            if(instance != null) {
                if(instance.isPortalsReady()) {
                    if(p.getLocation().distance(instance.leftPortal()) < 2 || p.getLocation().distance(instance.rightPortal()) < 2) {
                        instance.onEnterPortal();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if(Helper.randInt(0, 1000) < 7) {
            e.setCancelled(true);
            e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), GiveRewardItemCommand.getKey());
        }
        if(Helper.randInt(0, 1000) < 12) {
            SplinterManager.addSplinter(e.getPlayer());
        }
    }
}
