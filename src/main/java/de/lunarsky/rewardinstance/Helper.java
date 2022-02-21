package de.lunarsky.rewardinstance;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Random;

public class Helper {
    /**
     * Helper Method to faster create Items with displayname/lore in a single line
     * @param material
     * @param name
     * @param lore
     * @return the ItemStack
     */
    public static ItemStack createSimpleIcon(Material material, String name, String[] lore) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        stack.setItemMeta(meta);
        return stack;
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static int randInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    /**
     * Pastes a WorldEdit .schem File at a location, only works with Fawe (!)
     * @param loc           location
     * @param schematic     .schem File
     * @param ignoreAir     if air should not be pasted
     */
    public static void pasteSchematic(Location loc, File schematic, boolean ignoreAir) {
        try (EditSession editSession = com.sk89q.worldedit.WorldEdit.getInstance().getEditSessionFactory().getEditSession(FaweAPI.getWorld(loc.getWorld().getName()), -1)) {
            ClipboardFormat format = ClipboardFormats.findByFile(schematic);
            ClipboardReader reader = format.getReader(new FileInputStream(schematic));
            ClipboardHolder holder = new ClipboardHolder(format.getReader(new FileInputStream(schematic)).read());
            Operation op = holder
                    .createPaste(editSession)
                    .to(BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
                    .copyEntities(false)
                    .ignoreAirBlocks(ignoreAir)
                    .build();
            Operations.complete(op);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
