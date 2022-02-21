package de.lunarsky.rewardinstance;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InstanceLoot implements CommandExecutor {
    private static ArrayList<ItemStack> loot;

    public static void fillLoot() {
        loot = new ArrayList<ItemStack>();
        for(int i = 0; i < Helper.randInt(2, 5); i++) {
            ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
            ItemMeta meta = pick.getItemMeta();
            pick.addUnsafeEnchantment(Enchantment.DURABILITY, Helper.randInt(1, 4));
            pick.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, Helper.randInt(1, 2));
            pick.addUnsafeEnchantment(Enchantment.DIG_SPEED, Helper.randInt(1, 6));
            if(Helper.randInt(0, 10) <= 2) pick.addUnsafeEnchantment(Enchantment.MENDING, 1);
            loot.add(pick);
        }


        for(int i = 0; i < Helper.randInt(2, 5); i++) {
            ItemStack pick = new ItemStack(Material.IRON_PICKAXE);
            ItemMeta meta = pick.getItemMeta();
            pick.addUnsafeEnchantment(Enchantment.DURABILITY, Helper.randInt(1, 4));
            pick.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, Helper.randInt(1, 2));
            pick.addUnsafeEnchantment(Enchantment.DIG_SPEED, Helper.randInt(4, 6));
            if(Helper.randInt(0, 10) <= 3) pick.addUnsafeEnchantment(Enchantment.MENDING, 1);
            loot.add(pick);
        }


        for(int i = 0; i < Helper.randInt(2, 5); i++) {
            ItemStack axe = new ItemStack(Material.IRON_AXE);
            ItemMeta meta = axe.getItemMeta();
            axe.addUnsafeEnchantment(Enchantment.DURABILITY, Helper.randInt(2, 4));
            axe.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, Helper.randInt(1, 2));
            axe.addUnsafeEnchantment(Enchantment.DIG_SPEED, Helper.randInt(4, 6));
            if(Helper.randInt(0, 10) <= 3) axe.addUnsafeEnchantment(Enchantment.MENDING, 1);
            loot.add(axe);
        }

        for(int i = 0; i < Helper.randInt(2, 5); i++) {
            ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
            ItemMeta meta = axe.getItemMeta();
            axe.addUnsafeEnchantment(Enchantment.DURABILITY, Helper.randInt(1, 4));
            axe.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, Helper.randInt(1, 2));
            axe.addUnsafeEnchantment(Enchantment.DIG_SPEED, Helper.randInt(1, 6));
            if(Helper.randInt(0, 10) <= 2) axe.addUnsafeEnchantment(Enchantment.MENDING, 1);
            loot.add(axe);
        }

        for(int i = 0; i < Helper.randInt(4, 5); i++) {
            loot.add(new ItemStack(Material.NETHERITE_SCRAP, Helper.randInt(1, 3)));
        }

        for(int i = 0; i < Helper.randInt(4, 6); i++) {
            loot.add(new ItemStack(Material.PHANTOM_MEMBRANE, Helper.randInt(4, 12)));
            loot.add(new ItemStack(Material.DIAMOND, Helper.randInt(4, 20)));
            loot.add(new ItemStack(Material.GOLD_INGOT, Helper.randInt(4, 20)));
            loot.add(new ItemStack(Material.EMERALD, Helper.randInt(4, 20)));
            loot.add(new ItemStack(Material.GOLDEN_APPLE, Helper.randInt(2, 5)));
            loot.add(new ItemStack(Material.GOLDEN_CARROT, Helper.randInt(16, 30)));
        }

        for(int i = 0; i < Helper.randInt(2, 3); i++) {
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
            meta.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Helper.randInt(1, 5), true);
            book.setItemMeta(meta);
            loot.add(book);

            book = new ItemStack(Material.ENCHANTED_BOOK);
            meta = (EnchantmentStorageMeta) book.getItemMeta();
            meta.addStoredEnchant(Enchantment.DAMAGE_ALL, Helper.randInt(2, 6), true);
            book.setItemMeta(meta);
            loot.add(book);

            book = new ItemStack(Material.ENCHANTED_BOOK);
            meta = (EnchantmentStorageMeta) book.getItemMeta();
            meta.addStoredEnchant(Enchantment.DIG_SPEED, Helper.randInt(3, 6), true);
            book.setItemMeta(meta);
            loot.add(book);

            book = new ItemStack(Material.ENCHANTED_BOOK);
            meta = (EnchantmentStorageMeta) book.getItemMeta();
            meta.addStoredEnchant(Enchantment.MENDING, 1, true);
            book.setItemMeta(meta);
            loot.add(book);

            loot.add(new ItemStack(Material.DRAGON_BREATH, Helper.randInt(2, 6)));
        }
    }

    public static ArrayList<ItemStack> getLoot() {
        return loot;
    }

    /**
     * -
     * @return      1 random item from loot list (must be filled before)
     */
    public static ItemStack getRandomItem() {
        return loot.get(Helper.randInt(0, loot.size() - 1));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player p) {
            if(p.hasPermission("lunarsky.instance.peekloot")) {
                World w = p.getWorld();
                getLoot().forEach(item -> w.dropItem(p.getLocation(), item));
            }
        }
        return false;
    }
}
