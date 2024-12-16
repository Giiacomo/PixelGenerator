package me.giacomo.minecraft.pixelGenerator.events.generator;

import me.giacomo.minecraft.pixelGenerator.exceptions.MaterialNotBlockException;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorItem;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.NormalItemGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.handlers.GeneratorHandler;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.GeneratorInteractions;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.Messages;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GeneratorBlockListener implements Listener {
    @EventHandler
    public void onGeneratorPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (!Permissions.PLACE.has(player))
            return;

        ItemStack item = event.getItemInHand();
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            if (meta.getPersistentDataContainer().get(GeneratorItem.itemKey, PersistentDataType.STRING) != null) {
                GeneratorHandler.handleGeneratorPlacement(block, player, meta);
            }
        }
    }

    @EventHandler
    public void onGeneratorDestroy(BlockBreakEvent event) throws MaterialNotBlockException {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (    GeneratorManager.isGenerator(block)
                && !GeneratorInteractions.CAN_BREAK.getConfigValue()
                && !Permissions.BREAK.has(player)) {
            Utilities.warnPlayer(player, Messages.NO_PERMISSION.getValue());
            event.setCancelled(true);
            return;
        }

        GeneratorHandler.handleDestroyGenerator(block, player);
        event.setDropItems(false);
    }

}
