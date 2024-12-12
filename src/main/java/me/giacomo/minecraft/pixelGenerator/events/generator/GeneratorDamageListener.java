package me.giacomo.minecraft.pixelGenerator.events.generator;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import me.giacomo.minecraft.pixelGenerator.handlers.GeneratorHandler;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.GeneratorInteractions;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

import static me.giacomo.minecraft.pixelGenerator.handlers.GeneratorHandler.handleDestroyGenerator;

public class GeneratorDamageListener implements Listener {

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (!GeneratorInteractions.CAN_EXPLODE.getConfigValue()) {
            event.blockList().removeIf(GeneratorManager::isGenerator);
            return;
        }
        event.blockList().forEach(block -> handleDestroyGenerator(block, null));

        if (event.blockList().stream().anyMatch(GeneratorManager::isGenerator)) {
            event.setYield(0f);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!GeneratorInteractions.CAN_EXPLODE.getConfigValue()) {
            event.blockList().removeIf(GeneratorManager::isGenerator);
            return;
        }
        event.blockList().forEach(block -> handleDestroyGenerator(block, null));

        if (event.blockList().stream().anyMatch(GeneratorManager::isGenerator)) {
            event.setYield(0f);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (!GeneratorInteractions.CAN_MOVE_BY_PISTONS.getConfigValue() &&
                event.getBlocks().stream().anyMatch(GeneratorManager::isGenerator)) {
            event.setCancelled(true);
            return;
        }

        GeneratorHandler.handlePistonInteractions(event.getBlocks(), event.getDirection());
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (!GeneratorInteractions.CAN_MOVE_BY_PISTONS.getConfigValue() &&
            event.getBlocks().stream().anyMatch(GeneratorManager::isGenerator)) {
            event.setCancelled(true);
        }

        GeneratorHandler.handlePistonInteractions(event.getBlocks(), event.getDirection());
    }


    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (!GeneratorInteractions.CAN_BURN.getConfigValue() &&
        GeneratorManager.isGenerator(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockLavaDestroy(BlockFromToEvent event) {
        if (!GeneratorInteractions.CAN_LAVA_DESTROY.getConfigValue() &&
            GeneratorManager.isGenerator(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!GeneratorInteractions.CAN_ENTITIES_MOVE.getConfigValue() &&
            GeneratorManager.isGenerator(event.getBlock())) {
            event.setCancelled(true);
        }
    }



}
