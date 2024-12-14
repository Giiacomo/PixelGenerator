package me.giacomo.minecraft.pixelGenerator.generators.items;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GenerableItemManager {
    private static HashMap<String, ItemStack> items = new HashMap<>();

    public static void loadGenerableItems() {
        FileConfiguration config = PixelGenerator.getInstance().getConfig();
        // Check if the 'custom-items' section exists and is not null
        if (config.contains("custom-items")) {
            for (String key : config.getConfigurationSection("custom-items").getKeys(false)) {
                String path = "custom-items." + key;

                // Load item properties
                String name = config.getString(path + ".name", "Unnamed Item");

                List<String> lore = config.getStringList(path + ".lore-lines");
                String material = config.getString(path + ".material", "DIAMOND");
                ItemStack item = new ItemBuilder(Material.getMaterial(material))
                        .setDisplayName(name)
                        .addLoreLines(lore.toArray(new String[0]))
                        .addLoreLines(key)
                        .get();

                items.put(key.toUpperCase(), item);
            }
            Bukkit.getLogger().info("Custom items loaded successfully.");
        } else {
            Bukkit.getLogger().warning("No custom items found in configuration or the 'custom-items' section is missing.");
        }
    }

    public static List<String> getAllItemNames() {
        return items.keySet().stream().collect(Collectors.toList());
    }

    public static ItemStack getItem(String key) {
        return items.getOrDefault(normalizeKey(key), null);
    }


    private static String normalizeKey(String key) {
        return key.replaceAll("§[0-9a-fk-or]", "").toUpperCase();
    }
}
