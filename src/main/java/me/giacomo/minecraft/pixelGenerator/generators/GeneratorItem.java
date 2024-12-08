package me.giacomo.minecraft.pixelGenerator.generators;

import me.giacomo.minecraft.pixelGenerator.exceptions.MaterialNotBlockException;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class GeneratorItem {

    public static final NamespacedKey itemKey = new NamespacedKey("generator", "item");
    public static final NamespacedKey intervalKey = new NamespacedKey("generator", "interval");
    public static final NamespacedKey quantityKey = new NamespacedKey("generator", "quantity");


    public static ItemStack createGeneratorItem(String block, String itemName, int interval, int quantity) throws MaterialNotBlockException, IllegalArgumentException {
        Material blockMaterial = Material.getMaterial(block);
        Material itemMaterial = Material.getMaterial(itemName);

        if (blockMaterial == null) {
            throw new IllegalArgumentException("Invalid block: " + block);
        }

        if (itemMaterial == null) {
            throw new IllegalArgumentException("Invalid item: " + itemName);
        }

        if (interval <= 0 || quantity <= 0)
            throw new IllegalArgumentException("Interval and quantity need to be positive integers");

        if (!blockMaterial.isBlock())
            throw new MaterialNotBlockException(blockMaterial.name() + " is not a block");


        ItemStack item = new ItemStack(blockMaterial);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§aItem Generator");
            meta.setLore(Arrays.asList("§7Generates " + itemName + " every " + interval + " seconds", "§7Right-click to place"));
            meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, itemName);
            meta.getPersistentDataContainer().set(intervalKey, PersistentDataType.INTEGER, interval);
            meta.getPersistentDataContainer().set(quantityKey, PersistentDataType.INTEGER, quantity);
            item.setItemMeta(meta);
        }

        return item;
    }
}
