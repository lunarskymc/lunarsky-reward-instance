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
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class Helper {

    /**
     * creatas a text component with custom color
     * @param msg       text
     * @param color     color in hex
     * @return          textcomponent colored
     */
    public static TextComponent c(String msg, String color) {
        TextComponent text = new TextComponent(msg);
        text.setColor(ChatColor.of(color));
        return text;
    }

    /**
     * adds two text components and returns both combined (helper method)
     * @param a     TextComponent 1
     * @param b     TextComponent 2
     * @return      result
     */
    public static TextComponent c(TextComponent a, TextComponent b) {
        a.addExtra(b);
        return a;
    }

    /**
     * Helper Method to faster create Items with displayname/lore in a single line
     * @param material  type
     * @param name      displayname
     * @param lore      lore lines
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
    public static void displayParticleTwirl(Player p, Location loc, double angel, Color color) {
        Location[] points = Helper.getPointsOnCircle(loc, 1, 20, angel);
        for (Location pointlocation : points) {
            p.spawnParticle(Particle.REDSTONE, pointlocation, 0, new Particle.DustOptions(color, 1f));
        }
    }


    /**
     * returns an array of locations on a circle around a point
     * @param center        circle center M
     * @param radius        radius r
     * @param points        amount of points on the circle
     * @param startangel    start angel
     * @return              array of locations for each point on the circle
     */
    public static Location[] getPointsOnCircle(Location center, double radius, int points, double startangel) {
        Location[] locations = new Location[points];
        World w = center.getWorld();

        double cy = center.getY();
        double cx = center.getX();
        double cz = center.getZ();

        for(int i = 0; i < points; ++i) {
            final double angle = Math.toRadians(((double) i / points) * 360d) + startangel;
            locations[i] = new Location(w,
                    cx + Math.cos(angle) * radius,
                    cy,
                    cz + Math.sin(angle) * radius
            );
        }
        return locations;
    }

    /**
     * returns a filled circle with locations
     * @param center        center
     * @param radius        radius
     * @return              ArrayList of Locations, starting in the middle then going out
     */
    public static ArrayList<Location> getCircularShape(Location center, double radius) {
        ArrayList<Location> locations = new ArrayList<>();
        for(double i = 0.1; i < radius; i += 0.3) {
            Location[] points = getPointsOnCircle(center, i, (int) (i * 15), 0);
            for(Location l : points) {
                locations.add(l);
            }
        }
        return locations;
    }
}
