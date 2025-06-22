package de.lunarsky.rewardinstance;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import de.lunarsky.rewardinstance.core.RewardInstancePlugin;
import de.lunarsky.rewardinstance.events.AttackDamager;
import eu.decentsoftware.holograms.api.DHAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Instance {
    public static final String lootInventoryName = "§3§lLoot";
    private Player p;
    private int id, stage;
    private File schematic = new File("plugins/lunarsky-instance/arena.schem");
    private File noPortalSchem = new File("plugins/lunarsky-instance/noportals.schem");
    private File portalSchem = new File("plugins/lunarsky-instance/portals.schem");
    private World world = Bukkit.getWorld(InstanceManager.worldname);
    private boolean chestLoot, mobsSpawned, portalsReady, failed;
    private Hologram hologram;
    private ArrayList<Entity> enemies = new ArrayList<>();
    private Long fightTimestampStart;
    private int secondsrunning = 0;
    private int schedulerid, chestparticlescheduler;
    private int angel = 0;

    public void loadSchematic() {
        new Thread(() -> Helper.pasteSchematic(getSpawn(), schematic, true)).start();
        new Thread(() -> Helper.pasteSchematic(getSpawn(), noPortalSchem, true)).start();
        startChestParticles();
    }

    /**
     * gets the schematic location / spawn
     * @return  schematic location, is calculated by x = id * 3000
     */
    public Location getSpawn() {
        return new Location(world, id * 3000 + 0.5, 120, 0.5);
    }

    /**
     * this method just starts a runnable that makes particles around the chest.
     * the runnable is stoped once mobs spawn
     */
    public void startChestParticles() {
        Location loc = getLootBlock().getLocation();
        loc.add(0.5, 0.12, 0.5);
        chestparticlescheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(RewardInstancePlugin.getInstance(), () -> {
            angel += 1;
            if(angel > 360) angel = 0;
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getWorld() == loc.getWorld())
                    .filter(p -> p.getLocation().distance(loc) < 100)
                    .forEach(p -> Helper.displayParticleTwirl(p, loc, angel, Color.fromRGB(235, 235, 235)));
            loc.getWorld().spawnParticle(Particle.END_ROD, getLootBlock().getLocation().add(0.5, 0.5, 0.5), 5);
        }, 20L, 5L);
    }

    /**
     * is called when a player enters a portal
     * decides randomly which portal is the correct one,
     * maybe replace this random stuff with a cool
     * minigame or something.
     */
    public void onEnterPortal() {
        int chance = Helper.randInt(0, 100);
        if(chance < 100 - (stage - 1) * 10) {
            p.sendTitle("§a✔", "§7Richtiges Portal!", 10, 20, 15);
            nextRoom();
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
        else {
            p.sendTitle("§c✖", "§7falsches Portal!", 10, 20, 15);
            p.sendMessage("§7Ups, das war leider das falsche Portal.");
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, 4, true));
            onFail();
        }
    }

    /**
     * when the player fails, they should be teleported back to spawn.
     */
    public void onFail() {
        failed = true;
        p.sendMessage("§7Der Inhalt ist gescheitert. Du wirst zum Spawn teleportiert.");
        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
        Bukkit.dispatchCommand(p, "spawn");
        hologram.delete();
        Bukkit.getScheduler().cancelTask(schedulerid);
        InstanceManager.removeInstance(id);
    }

    /**
     * called when all enemies are killed.
     * a hologram should appear with instructions on
     * how to use the portals
     * the portals should become usable at this point.
     */
    public void onAllEnemiesKilled() {
        if(failed) return;
        p.sendTitle("§a✔", "§7Alle Gegner besiegt!", 10, 20, 15);
        p.sendMessage("§7Du hast alle Gegner besiegt. Es ist ein Hologramm mit Anweisungen erschienen.");
        new Thread(() -> Helper.pasteSchematic(getSpawn(), portalSchem, false)).start();

        hologram = DHAPI.createHologram("instance_" + UUID.randomUUID().toString(), hologramLocation());
        DHAPI.addHologramLine(hologram,  new ItemStack(Material.LIGHT));
        DHAPI.addHologramLine(hologram, "§bHerzlichen Glückwunsch!");
        DHAPI.addHologramLine(hologram, "§7Du hast alle Gegner besiegt.");
        DHAPI.addHologramLine(hologram, "§7Wähle nun eines der Portale,");
        DHAPI.addHologramLine(hologram, "§7um in den nächsten Raum zu");
        DHAPI.addHologramLine(hologram, "§7gelangen.");
        DHAPI.addHologramLine(hologram, "§cABER VORSICHT!");
        DHAPI.addHologramLine(hologram, "§7Wählst du das falsche Portal,");
        DHAPI.addHologramLine(hologram, "§7ist alles vorbei und du wirst");
        DHAPI.addHologramLine(hologram, "§7zurück zum Spawn teleportiert.");

        Bukkit.getScheduler().cancelTask(schedulerid);
        portalsReady = true;
        schedulerid = Bukkit.getScheduler().scheduleSyncRepeatingTask(RewardInstancePlugin.getInstance(), () -> {
            world.spawnParticle(Particle.END_ROD, leftPortal(), 5);
            world.spawnParticle(Particle.END_ROD, rightPortal(), 5);
        }, 1L, 1L);
    }

    /**
     * resets the room, teleports player back to beginning portal,
     * also new loot and new mobs on opening
     */
    public void nextRoom() {
        new Thread(() -> Helper.pasteSchematic(getSpawn(), noPortalSchem, false)).start();
        stage++;
        p.teleport(getSpawn());
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 3, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 10, 3, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 10, true));
        p.spawnParticle(Particle.EXPLOSION, p.getLocation(), 5);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
        p.sendMessage("§7Du hast den nächsten Raum betreten.");
        chestLoot = true;
        portalsReady = false;
        mobsSpawned = false;
        hologram.delete();
        Bukkit.getScheduler().cancelTask(schedulerid);
        startChestParticles();
    }

    /**
     * creates a circular damage attack and displays it, the damage is dealt within AttackDamager (a Listener)
     * @param loc       location
     * @param size      size
     * @param time      time in seconds to last
     */
    public void circularDamageAttack(Location loc, int size, int time) {
        ArrayList<Location> locations = Helper.getCircularShape(loc, size);
        World world = locations.get(0).getWorld();
        int schid = Bukkit.getScheduler().scheduleSyncRepeatingTask(RewardInstancePlugin.getInstance(), () -> {
            new Thread(() -> {
                locations.forEach(particleLocation -> {
                    Color color = Color.fromRGB(191, 46, 46);
                    p.spawnParticle(Particle.FLAME, particleLocation, 0, new Particle.DustOptions(color, 3f));
                });
            }).start();
        }, 5L, 5L);
        String uuid = UUID.randomUUID().toString();
        AttackDamager.addInfo(new CircularDamageInfo(uuid, loc, size, id));
        Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> {
            Bukkit.getScheduler().cancelTask(schid);
            AttackDamager.removeInfo(uuid);
        }, time * 20L);
    }

    /**
     * this method should be called when the player closes the loot chest inventory
     * (found by inventory name)
     * the chest explodes and some mobs spawn
     * the mobs also drop some loot randomly
     * the amount of mobs increases with each stage so it actually gets harder to survive
     * @param cause     the user that caused the mobs to spawn
     */
    public void spawnMobs(Player cause) {
        if(!mobsSpawned) {
            mobsSpawned = true;
            Bukkit.getScheduler().cancelTask(chestparticlescheduler);
            p.sendMessage("§7Kämpfe dich durch die Gegner ohne zu sterben! §cDu hast 4 Minuten Zeit!");
            p.sendTitle("§c⚔", "§7Kämpfe!", 10, 20, 15);
            secondsrunning = 0;
            fightTimestampStart = System.currentTimeMillis();
            enemies = new ArrayList<>();
            Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> {
                world.createExplosion(getLootBlock().getLocation(), 5, false, false);
                int mobamount = stage + Helper.randInt(1, 4);
                if(stage == 1) mobamount = 1;
                if(stage == 2) mobamount = 2;
                for(int i = 0; i < mobamount; i++) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(RewardInstancePlugin.getInstance(), () -> {
                        Location loc = randomMobLocation();
                        world.spawnParticle(Particle.END_ROD, loc, 12);
                        enemies.add(InstanceEnemies.SpawnEnemy(loc, stage, cause));
                    }, i * 10L);
                }
            }, 10L);
            schedulerid = Bukkit.getScheduler().scheduleSyncRepeatingTask(RewardInstancePlugin.getInstance(), () -> {
                secondsrunning++;
                enemies.stream()
                        .filter(Objects::nonNull)
                        .filter(e -> !e.isDead())
                        .filter(e -> (!(e instanceof Vex) && e.getLocation().getY() < 120) || e.getLocation().distance(getLootBlock().getLocation()) > 25)
                        .forEach(e -> {
                            enemies.remove(e);
                            e.remove();
                        });
                /* //todo: make those working
                if(secondsrunning == 5) circularDamageAttack(new Location(world, id * 3000 + 2, 120, id * 3000 - 2), 4, 20);
                if(secondsrunning == 5 && stage > 1) circularDamageAttack(new Location(world, id * 3000 - 7, 120, id * 3000 + 6), 4, 60);
                if(secondsrunning == 19 && stage > 2) circularDamageAttack(new Location(world, id * 3000 + 4, 120, id * 3000 + 10), 4, 60);*/
                if(enemies.stream().filter(e -> !e.isDead()).count() == 0 && secondsrunning >= 10) {
                    onAllEnemiesKilled();
                }
            }, 0L, 20L);
        }
    }

    /**
     * creates a random location within 13 blocks from the center of the map (on ground) for mobs to spawn
     * @return      the location
     */
    private Location randomMobLocation() {
        int r = 13;
        int x = Helper.randInt(0, r * 2) - r;
        int z = Helper.randInt(0, r * 2) - r;
        return new Location(world, id * 3000 + x, getSpawn().getY(), z);
    }

    /**
     * this method should generate loot for each stage (higher stage -> better loot)
     * if this.chestLoot is false, the Inventory will be empty (when the player already collected their loot)
     * @return the inventory
     */
    public Inventory getLootInventory() {
        Inventory inv = Bukkit.createInventory(null, 9*3, lootInventoryName);
        if(chestLoot) {

            int itemamount = (int) (stage * 1.4 + Helper.randInt(1, 5));
            for(int i = 0; i < itemamount; i++) {
                inv.setItem(Helper.randInt(0, inv.getSize() - 1), InstanceLoot.getRandomItem(stage));
            }
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
            chestLoot = false;
        }
        return inv;
    }

    public Instance(Player p, int id) {
        this.p = p;
        this.id = id;
        this.stage = 1;
        this.chestLoot = true;
        this.mobsSpawned = false;
        this.portalsReady = false;
    }

    public Block getLootBlock() {
        return world.getBlockAt(id * 3000, 121, 9);
    }

    public Location leftPortal() {
        return new Location(world, id * 3000 + 4.5, 120, 22.6);
    }

    public Location rightPortal() {
        return new Location(world, id * 3000 - 4.5, 120, 22.6);
    }

    public Location hologramLocation() {
        return new Location(world, id * 3000 + 0.5, 123.4, 19.5);
    }

    public Player getP() {
        return p;
    }

    public int getId() {
        return id;
    }

    public World getWorld() {
        return this.world;
    }

    public ArrayList<Entity> getEnemies() {
        return enemies;
    }

    public int getEnemiesCount() {
        return enemies.size();
    }

    public int getRemainingEnemiesCount() {
        if(enemies.size() == 0) return 0;
        return (int) (enemies.stream().filter(e -> !e.isDead()).count());
    }

    public Hologram getHologram() {
        return hologram;
    }

    public boolean isPortalsReady() {
        return portalsReady;
    }

    public int getStage() {
        return stage;
    }

    public int timeRunning() {
        return secondsrunning;
    }
}
