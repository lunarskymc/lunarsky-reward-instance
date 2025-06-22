package de.lunarsky.rewardinstance;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InstanceEnemies {
    /**
     * Spawns a random entity (stronger each stage)
     * @param loc       where
     * @param stage     stage
     * @param target    the player it should target
     * @return          the entity afte rspawning
     */
    public static Entity SpawnEnemy(Location loc, int stage, Player target) {
        World world = loc.getWorld();
        int r = Helper.randInt(0, 2);
        if(r == 0) {
            Skeleton mob = (Skeleton) world.spawnEntity(loc, EntityType.SKELETON);
            mob.setMaxHealth(50 + stage * 8 + Helper.randInt(1, 6));
            mob.setHealth(mob.getMaxHealth());
            mob.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 1 + (int) (stage / 1.4), true));
            mob.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
            mob.setTarget(target);
            return mob;
        }
        else if(r == 1) {
            WitherSkeleton mob = (WitherSkeleton) world.spawnEntity(loc, EntityType.WITHER_SKELETON);
            mob.setMaxHealth(50 + stage * 8 + Helper.randInt(1, 6));
            mob.setHealth(mob.getMaxHealth());
            mob.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 1, false));
            mob.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1000000, 1 + (int) (stage / 1.4), true));
            mob.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
            mob.setTarget(target);
            return mob;
        }
        else if(r == 2 && stage > 3) {
            Vex mob = (Vex) world.spawnEntity(loc, EntityType.VEX);
            mob.setMaxHealth(25 + stage * 8 + Helper.randInt(1, 6));
            mob.setHealth(mob.getMaxHealth());
            mob.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, Math.min(1 + (int) (stage / 1.4), 3), true));
            mob.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
            mob.setTarget(target);
            return mob;
        }
        else if(r == 2) {
            Phantom mob = (Phantom) world.spawnEntity(loc, EntityType.WITHER_SKELETON);
            int size = Helper.randInt(2, 5);
            mob.setMaxHealth(size * 8 + stage);
            mob.setHealth(mob.getMaxHealth());
            mob.setSize(size);
            mob.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 1 + (int) (stage / 1.4), true));
            mob.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
            mob.setTarget(target);
            return mob;
        }

        return null;
    }
}
