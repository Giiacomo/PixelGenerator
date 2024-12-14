package me.giacomo.minecraft.pixelGenerator.handlers;

import me.giacomo.minecraft.pixelGenerator.exceptions.MaterialNotBlockException;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorItem;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.CustomItemGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.NormalItemGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.items.GenerableItemManager;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.Messages;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GeneratorHandler {

    public static void handleGeneratorPlacement(Block block, Player player, ItemMeta meta) {
        String materialStr = meta.getPersistentDataContainer().get(GeneratorItem.itemKey, PersistentDataType.STRING);
        Integer interval = meta.getPersistentDataContainer().get(GeneratorItem.intervalKey, PersistentDataType.INTEGER);
        Integer quantity = meta.getPersistentDataContainer().get(GeneratorItem.quantityKey, PersistentDataType.INTEGER);
        Material generatedItem = Material.getMaterial(materialStr);

        if (generatedItem == null) {
            handleCustomItemGeneratorPlacement(block, materialStr, interval, quantity);
        } else {
            AbstractGeneratorBlock<Material> generator = new NormalItemGeneratorBlock(block, generatedItem, interval, quantity);
            GeneratorManager.addGenerator(generator);
        }
        Utilities.informPlayer(player, Messages.PLAYER_PLACE_GENERATOR.getValue());
    }

    private static void handleCustomItemGeneratorPlacement(Block block, String customItem, int interval, int quantity) {
        ItemStack generatedItem = GenerableItemManager.getItem(customItem);
        AbstractGeneratorBlock<ItemStack> generator = new CustomItemGeneratorBlock(block, generatedItem, interval, quantity);
        GeneratorManager.addGenerator(generator);
    }

    public static void handleDestroyGenerator(Block block, Player player) {
        if (!GeneratorManager.isGenerator(block)) {
            return;
        }
        try {
            if (player == null || !player.getGameMode().toString().equals(GameMode.CREATIVE.toString())) {
                ItemStack generatorItem = GeneratorItem.createGeneratorItemFromGeneratorBlock(GeneratorManager.findByBlock(block));
                block.getWorld().dropItemNaturally(block.getLocation(), generatorItem);
            }
            if (player != null)
                Utilities.informPlayer(player, Messages.PLAYER_DESTROY_GENERATOR.getValue());

            GeneratorManager.removeGenerator(block);
        } catch (MaterialNotBlockException e) {
            e.printStackTrace();
        }
    }

    public static void handlePistonInteractions(List<Block> blocks, BlockFace direction) {
        for (Block block : blocks) {
            if (GeneratorManager.isGenerator(block)) {
                AbstractGeneratorBlock generatorBlock = GeneratorManager.findByBlock(block);

                if (generatorBlock != null) {
                    Block newBlock = block.getRelative(direction);
                    GeneratorManager.updateGeneratorPosition(generatorBlock, newBlock);
                }
            }
        }
    }


}
