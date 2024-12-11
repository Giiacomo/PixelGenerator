package me.giacomo.minecraft.pixelGenerator.generators;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.exceptions.MaterialNotBlockException;
import me.giacomo.minecraft.pixelGenerator.helpers.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
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
            throw new IllegalArgumentException(Message.INVALID_BLOCK_EXCEPTION.getValue() + ": " + block);
        }

        if (itemMaterial == null) {
            throw new IllegalArgumentException(Message.INVALID_ITEM_EXCEPTION.getValue() + ": " + itemName);
        }

        if (interval <= 0 || quantity <= 0)
            throw new IllegalArgumentException(Message.PARAMETER_POSITIVE_INTEGER_EXCEPTION.getValue());

        if (!blockMaterial.isBlock())
            throw new MaterialNotBlockException(Message.IS_NOT_A_BLOCK_EXCEPTION.getValue() + ": "  + blockMaterial.name());


        ItemStack item = new ItemStack(blockMaterial);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(Message.ITEM_GENERATOR_NAME.getValue());
            meta.setLore(Arrays.asList("§7Generates " + quantity + " " + itemName.toLowerCase().replace('_', ' ') + " every " + interval + " seconds", "§7Right-click to place"));
            meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, itemName);
            meta.getPersistentDataContainer().set(intervalKey, PersistentDataType.INTEGER, interval);
            meta.getPersistentDataContainer().set(quantityKey, PersistentDataType.INTEGER, quantity);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createGeneratorItemFromGeneratorBlock(GeneratorBlock generatorBlock) throws MaterialNotBlockException {
        return createGeneratorItem(generatorBlock.getBlock().getType().name(),
                                generatorBlock.getItemToGenerate().name(),
                                generatorBlock.getInterval(),
                                generatorBlock.getQuantity());
    }
}
