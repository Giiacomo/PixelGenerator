package me.giacomo.minecraft.pixelGenerator.helpers;


import com.cryptomorin.xseries.XMaterial;
import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import org.bukkit.entity.Player;
import com.google.common.io.ByteStreams;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public final class Utilities {

    private static final Set<Material> TRANSPARENT = EnumSet.of(XMaterial.AIR.parseMaterial(), XMaterial.BLACK_CARPET.parseMaterial(), XMaterial.BLUE_CARPET.parseMaterial(),
            XMaterial.BROWN_CARPET.parseMaterial(), XMaterial.CYAN_CARPET.parseMaterial(), XMaterial.GRAY_CARPET.parseMaterial(), XMaterial.GREEN_CARPET.parseMaterial(), XMaterial.LIGHT_BLUE_CARPET.parseMaterial(),
            XMaterial.LIME_CARPET.parseMaterial(), XMaterial.MAGENTA_CARPET.parseMaterial(), XMaterial.ORANGE_CARPET.parseMaterial(), XMaterial.PINK_CARPET.parseMaterial(), XMaterial.PURPLE_CARPET.parseMaterial(),
            XMaterial.RED_CARPET.parseMaterial(), XMaterial.WHITE_CARPET.parseMaterial(), XMaterial.YELLOW_CARPET.parseMaterial());

    public static final List<Material> INVENTORY_BLOCKS = Arrays.asList(XMaterial.CHEST.parseMaterial(),XMaterial.TRAPPED_CHEST.parseMaterial(), XMaterial.ENDER_CHEST.parseMaterial(), XMaterial.SHULKER_BOX.parseMaterial(), XMaterial.BLACK_SHULKER_BOX.parseMaterial(),
            XMaterial.BLUE_SHULKER_BOX.parseMaterial(), XMaterial.BROWN_SHULKER_BOX.parseMaterial(), XMaterial.CYAN_SHULKER_BOX.parseMaterial(), XMaterial.GRAY_SHULKER_BOX.parseMaterial(),
            XMaterial.GREEN_SHULKER_BOX.parseMaterial(), XMaterial.LIGHT_BLUE_SHULKER_BOX.parseMaterial(), XMaterial.LIGHT_GRAY_SHULKER_BOX.parseMaterial(), XMaterial.LIME_SHULKER_BOX.parseMaterial(),
            XMaterial.MAGENTA_SHULKER_BOX.parseMaterial(), XMaterial.ORANGE_SHULKER_BOX.parseMaterial(), XMaterial.PINK_SHULKER_BOX.parseMaterial(), XMaterial.PURPLE_SHULKER_BOX.parseMaterial(),
            XMaterial.RED_SHULKER_BOX.parseMaterial(), XMaterial.WHITE_SHULKER_BOX.parseMaterial(), XMaterial.YELLOW_SHULKER_BOX.parseMaterial());

    private static Map<Player, Long> mostRecentSelect = new HashMap<>();

    public static File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResource(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

    public static String toLocString(Location location) {
        if (location == null) return "";
        return location.getWorld().getName() + "," + (int)location.getX() + "," + (int)location.getY() + "," + (int)location.getZ();
    }

    public static ItemStack nameItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack nameItem(Material item, String name) {
        return nameItem(new ItemStack(item), name);
    }

    public static ItemStack loreItem(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static void warnPlayer(CommandSender sender, List<String> messages) {
        if (sender instanceof Player) { Player player = (Player) sender; playSound(ActionSound.ERROR, player); }
        for (String message : messages) sender.sendMessage(PixelGenerator.prefix + ChatColor.RESET + ChatColor.RED + message);
    }
    public static void warnPlayer(CommandSender sender, String message) {
        warnPlayer(sender, Collections.singletonList(message));
    }

    public static void informPlayer(CommandSender sender, List<String> messages) {
        for (String message : messages) sender.sendMessage(PixelGenerator.prefix + ChatColor.RESET + ChatColor.GRAY + message);
    }
    public static void informPlayer(CommandSender sender, String message) {
        informPlayer(sender, Collections.singletonList(message));
    }

    public static Block getBlockLookingAt(Player player) {
        return player.getTargetBlock(TRANSPARENT, 120);
    }

    public static void playSound(ActionSound sound, Player player) {

        switch (sound) {
            case OPEN:
                Sound.CHEST_OPEN.playSound(player);
                break;
            case MODIFY:
                Sound.ANVIL_USE.playSound(player);
                break;
            case SELECT:
                Sound.LEVEL_UP.playSound(player);
                break;
            case CLICK:
                Sound.CLICK.playSound(player);
                break;
            case POP:
                Sound.CHICKEN_EGG_POP.playSound(player);
                break;
            case BREAK:
                Sound.ANVIL_LAND.playSound(player);
                break;
            case ERROR:
                Sound.BAT_DEATH.playSound(player);
                break;
        }

    }

}