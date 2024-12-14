package me.giacomo.minecraft.pixelGenerator.generators;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.exceptions.MaterialNotBlockException;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.CustomItemGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.NormalItemGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.items.GenerableItemManager;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class GeneratorItem {

    public static final NamespacedKey itemKey = new NamespacedKey(PixelGenerator.getInstance(), "item");
    public static final NamespacedKey intervalKey = new NamespacedKey(PixelGenerator.getInstance(), "interval");
    public static final NamespacedKey quantityKey = new NamespacedKey(PixelGenerator.getInstance(), "quantity");


    public static ItemStack createGeneratorItem(String block, String itemName, int interval, int quantity) throws MaterialNotBlockException, IllegalArgumentException {
        Material blockMaterial = Material.getMaterial(block);
        Material itemMaterial = Material.getMaterial(itemName);

        if (blockMaterial == null || blockMaterial == Material.AIR) {
            throw new IllegalArgumentException(Messages.INVALID_BLOCK_EXCEPTION.getValue() + ": " + block);
        }
        ItemStack customItem = null;
        if (itemMaterial == null) {
            customItem = GenerableItemManager.getItem(itemName);
            Bukkit.getLogger().severe(customItem.getItemMeta().getDisplayName());
            Bukkit.getLogger().severe(GenerableItemManager.getKey(customItem));
            if (customItem == null)
                throw new IllegalArgumentException(Messages.INVALID_ITEM_EXCEPTION.getValue() + ": " + itemName);
            else itemName = GenerableItemManager.getKey(customItem);
        }

        if (interval <= 0 || quantity <= 0)
            throw new IllegalArgumentException(Messages.PARAMETER_POSITIVE_INTEGER_EXCEPTION.getValue());

        if (!blockMaterial.isBlock())
            throw new MaterialNotBlockException(Messages.IS_NOT_A_BLOCK_EXCEPTION.getValue() + ": "  + blockMaterial.name());


        ItemStack item = new ItemStack(blockMaterial);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(Messages.ITEM_GENERATOR_NAME.getValue());
            meta.setLore(Arrays.asList("§7Generates " + quantity + " " + itemName.toLowerCase().replace('_', ' ') + " every " + interval + " seconds", "§7Right-click to place"));
            meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, itemName);
            meta.getPersistentDataContainer().set(intervalKey, PersistentDataType.INTEGER, interval);
            meta.getPersistentDataContainer().set(quantityKey, PersistentDataType.INTEGER, quantity);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static <T> ItemStack createGeneratorItemFromGeneratorBlock(AbstractGeneratorBlock<T> generatorBlock) throws MaterialNotBlockException {
        if (generatorBlock instanceof NormalItemGeneratorBlock normalGenerator) {
            return createGeneratorItem(
                    normalGenerator.getBlock().getType().name(),
                    normalGenerator.getItemToGenerateName(),
                    normalGenerator.getInterval(),
                    normalGenerator.getQuantity()
            );
        } else if (generatorBlock instanceof CustomItemGeneratorBlock customGenerator) {
            return createGeneratorItem(
                    customGenerator.getBlock().getType().name(),
                    customGenerator.getItemToGenerateName(),
                    customGenerator.getInterval(),
                    customGenerator.getQuantity()
            );
        }
        return null;
    }
}
