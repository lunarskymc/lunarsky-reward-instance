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
import java.util.HashMap;

public class InstanceLoot implements CommandExecutor {
    private static HashMap<Integer, ArrayList<ItemStack>> stageLoot;

    public static void fillLoot(int stage) {
        ArrayList<ItemStack> loot = new ArrayList<>();

        for(int i = 0; i < Helper.randInt(1, 3); i++) {
            ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
            ItemMeta meta = pick.getItemMeta();
            pick.addUnsafeEnchantment(Enchantment.DURABILITY, Helper.randInt(1, stage < 5 ? 3 : 4));
            pick.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, Helper.randInt(1, stage < 3 ? 1 : 3));
            pick.addUnsafeEnchantment(Enchantment.DIG_SPEED, Helper.randInt(1, stage < 7 ? 5 : 6));
            if(Helper.randInt(0, 100) <= (stage > 6 ? 10 : 20) && stage >= 6) pick.addUnsafeEnchantment(Enchantment.MENDING, 1);
            loot.add(pick);
        }
        for(int i = 0; i < Helper.randInt(2, 5); i++) {
            ItemStack pick = new ItemStack(Material.IRON_PICKAXE);
            ItemMeta meta = pick.getItemMeta();
            pick.addUnsafeEnchantment(Enchantment.DURABILITY, Helper.randInt(1, stage < 6 ? 3 : 4));
            pick.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, Helper.randInt(1, stage < 3 ? 1 : 3));
            pick.addUnsafeEnchantment(Enchantment.DIG_SPEED, Helper.randInt(1, stage < 4 ? 5 : 6));
            if(Helper.randInt(0, 100) <= (stage > 6 ? 10 : 20) && stage >= 5) pick.addUnsafeEnchantment(Enchantment.MENDING, 1);
            loot.add(pick);
        }
        for(int i = 0; i < Helper.randInt(2, 5); i++) {
            ItemStack axe = new ItemStack(Material.IRON_AXE);
            ItemMeta meta = axe.getItemMeta();
            axe.addUnsafeEnchantment(Enchantment.DURABILITY, Helper.randInt(2, stage < 6 ? 3 : 4));
            if(Helper.randInt(0, 10) <= 2) axe.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, Helper.randInt(1, stage < 3 ? 2 : 3));
            axe.addUnsafeEnchantment(Enchantment.DIG_SPEED, Helper.randInt(4, stage > 4 ? 6 : (stage >= 3 ? 5 : 3)));
            if(Helper.randInt(0, 10) <= 1) axe.addUnsafeEnchantment(Enchantment.MENDING, 1);
            loot.add(axe);
        }
        for(int i = 0; i < Helper.randInt(2, 3); i++) {
            ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
            ItemMeta meta = axe.getItemMeta();
            if(Helper.randInt(0, 30) <= (stage > 5 ? 4 : 6)) axe.addUnsafeEnchantment(Enchantment.DURABILITY, Helper.randInt(1, stage > 6 ? 4 : 3));
            if(stage > 4 || Helper.randInt(0, 10) <= 3) axe.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, Helper.randInt(1, stage > 5 ? 3 : 2));
            axe.addUnsafeEnchantment(Enchantment.DIG_SPEED, Helper.randInt(1, stage < 7 ? 6 : 5));
            if(Helper.randInt(0, 43) <= 2) axe.addUnsafeEnchantment(Enchantment.MENDING, 1);
            loot.add(axe);
        }
        for(int i = 0; i < Helper.randInt(4, 6); i++) {
            loot.add(new ItemStack(Material.NETHERITE_SCRAP, Helper.randInt(1, Math.min(stage, 4))));
        }
        for(int i = 0; i < Helper.randInt(3, stage < 5 ? 5 : 7); i++) {
            loot.add(new ItemStack(Material.PHANTOM_MEMBRANE, Helper.randInt(4, stage * 2)));
            loot.add(new ItemStack(Material.DIAMOND, Helper.randInt(4, 16)));
            loot.add(new ItemStack(Material.GOLD_INGOT, Helper.randInt(4, 20)));
            loot.add(new ItemStack(Material.EMERALD, Helper.randInt(4, 9)));
            if(stage <= 4) loot.add(new ItemStack(Material.GOLDEN_CARROT, Helper.randInt(16, 30)));
            if(stage <= 5) loot.add(new ItemStack(Material.GOLDEN_APPLE, Helper.randInt(1, 3)));
            loot.add(new ItemStack(Material.PRISMARINE_SHARD, Helper.randInt(5, 30)));
        }
        for(int i = 0; i < Helper.randInt(2, 3); i++) {
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
            meta.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Helper.randInt(1, stage > 5 ? 5 : stage > 3 ? 4 : 3), true);
            book.setItemMeta(meta);
            loot.add(book);

            book = new ItemStack(Material.ENCHANTED_BOOK);
            meta = (EnchantmentStorageMeta) book.getItemMeta();
            meta.addStoredEnchant(Enchantment.DAMAGE_ALL, Helper.randInt(2, Helper.randInt(1, stage > 7 ? 5 : stage > 2 ? 4 : 3)), true);
            book.setItemMeta(meta);
            loot.add(book);

            book = new ItemStack(Material.ENCHANTED_BOOK);
            meta = (EnchantmentStorageMeta) book.getItemMeta();
            meta.addStoredEnchant(Enchantment.DIG_SPEED, Helper.randInt(3, stage > 7 ? 6 : 5), true);
            book.setItemMeta(meta);
            loot.add(book);

            if(Helper.randInt(0, 10) <= 3) {
                book = new ItemStack(Material.ENCHANTED_BOOK);
                meta = (EnchantmentStorageMeta) book.getItemMeta();
                meta.addStoredEnchant(Enchantment.MENDING, 1, true);
                book.setItemMeta(meta);
                loot.add(book);
            }
            loot.add(new ItemStack(Material.DRAGON_BREATH, Helper.randInt(2, Math.max(5, stage))));
        }
        if(stage > 8 && Helper.randInt(0, 100) < 2) loot.add(createNewFederfly(120));
        stageLoot.put(stage, loot);
    }

    /**
     * gets all the loot for a stage as an ArrayList<ItemStack>
     * @param stage     the stage
     * @return          the loot
     */
    public static ArrayList<ItemStack> getLoot(int stage) {
        return stageLoot.getOrDefault(stage, new ArrayList<>());
    }

    /**
     * -
     * @return      1 random item from loot list (must be filled before)
     */
    public static ItemStack getRandomItem(int stage) {
        ArrayList<ItemStack> loot = getLoot(stage);
        return loot.get(Helper.randInt(0, loot.size() - 1));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        final String syntax = "§c/peekloot <int:stage>";
        if(sender instanceof Player p) {
            if(p.hasPermission("lunarsky.instance.peekloot")) {
                if(args.length != 1) {
                    p.sendMessage(syntax);
                    return false;
                }
                try {
                    World w = p.getWorld();
                    int stage = Integer.parseInt(args[0]);
                    getLoot(stage).forEach(item -> w.dropItem(p.getLocation(), item));
                    p.sendMessage("§aloot gedroppt!");
                }catch (NumberFormatException e1) {
                    p.sendMessage("§7\"stage\"§c muss eine Nummer sein!");
                }
            }
        }
        return false;
    }

    /**
     * creates a new federfly item
     * @param max_uses      max uses
     * @return              itemstack
     */
    public static ItemStack createNewFederfly(int max_uses) {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§b§lFederfly");
        meta.setLore(getFederflyLore(max_uses, max_uses));
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * just returns the correct federfly lore for each item
     * @param remaining_uses    remaining uses: int
     * @param max_uses          max uses : int
     * @return                  arraylist<stirng> (the lores for the itemstack)
     */
    public static ArrayList<String> getFederflyLore(int remaining_uses, int max_uses) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7Haltbarkeit: §b{durability_remaining} §7/ §3{durability_max}"
                .replace("{durability_remaining}", Integer.toString(remaining_uses))
                .replace("{durability_max}", Integer.toString(max_uses)));

        lore.add("§7Klicke mit dem Item in die Luft,");
        lore.add("§7um einen Boost zu erhalten");
        lore.add("§7der dich §bfliegen §7lässt!");
        lore.add("§7");
        lore.add("§bDieses Item kann mit Phantomhäuten");
        lore.add("§bam Amboss repariert werden!");

        return lore;
    }
}
