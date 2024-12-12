package me.giacomo.minecraft.pixelGenerator.handlers;

import me.giacomo.minecraft.pixelGenerator.exceptions.MaterialNotBlockException;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorItem;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.Messages;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GeneratorHandler {
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
                GeneratorBlock generatorBlock = GeneratorManager.findByBlock(block);

                if (generatorBlock != null) {
                    Block newBlock = block.getRelative(direction);
                    GeneratorManager.updateGeneratorPosition(generatorBlock, newBlock);
                }
            }
        }
    }


}
