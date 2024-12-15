package me.giacomo.minecraft.pixelGenerator.generators.items;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GenerableItemManager {
    public static final NamespacedKey itemKey = new NamespacedKey(PixelGenerator.getInstance(), "itemKey");

    private static HashMap<String, ItemStack> items = new HashMap<>();


    public static void loadGenerableItems() {
        FileConfiguration config = PixelGenerator.getInstance().getConfig();
        if (config.contains("custom-items")) {
            for (String key : config.getConfigurationSection("custom-items").getKeys(false)) {
                String path = "custom-items." + key;

                String name = config.getString(path + ".name", "Unnamed Item");
                name = ChatColor.translateAlternateColorCodes('&', name);
                List<String> lore = config.getStringList(path + ".lore-lines");
                lore.forEach(ll -> ll = ChatColor.translateAlternateColorCodes('&', ll));
                String material = config.getString(path + ".material", "DIAMOND");
                ItemStack item = new ItemBuilder(Material.getMaterial(material))
                        .setDisplayName(name)
                        .addLoreLines(lore.toArray(new String[0]))
                        .get();
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, key);
                item.setItemMeta(meta);

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
        return items.getOrDefault(key.toUpperCase(), null);
    }

    public static String getKey(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
    }

}
