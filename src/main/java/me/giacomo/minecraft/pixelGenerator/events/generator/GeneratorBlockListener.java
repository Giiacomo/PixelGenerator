package me.giacomo.minecraft.pixelGenerator.events.generator;

import me.giacomo.minecraft.pixelGenerator.exceptions.MaterialNotBlockException;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorItem;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import me.giacomo.minecraft.pixelGenerator.handlers.GeneratorHandler;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.GeneratorInteractions;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.Messages;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.Permissions;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GeneratorBlockListener implements Listener {
    @EventHandler
    public void onGeneratorPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!Permissions.PLACE.has(player))
            return;

        ItemStack item = event.getItemInHand();
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            if (meta.hasDisplayName() && meta.getDisplayName().equals(Messages.ITEM_GENERATOR_NAME.getValue())) {
                Material generatedItem = Material.getMaterial(meta.getPersistentDataContainer().get(GeneratorItem.itemKey, PersistentDataType.STRING));
                Integer interval = meta.getPersistentDataContainer().get(GeneratorItem.intervalKey, PersistentDataType.INTEGER);
                Integer quantity = meta.getPersistentDataContainer().get(GeneratorItem.quantityKey, PersistentDataType.INTEGER);
                Utilities.informPlayer(event.getPlayer(), Messages.PLAYER_PLACE_GENERATOR.getValue());
                GeneratorBlock generator = new GeneratorBlock(event.getBlockPlaced(), generatedItem, interval, quantity);
                GeneratorManager.addGenerator(generator);
            }
        }
    }

    @EventHandler
    public void onGeneratorDestroy(BlockBreakEvent event) throws MaterialNotBlockException {
        Player player = event.getPlayer();

        if (!GeneratorInteractions.CAN_BREAK.getConfigValue() || !Permissions.BREAK.has(player)) {
            event.setCancelled(true);
            return;
        }
        Block block = event.getBlock();
        GeneratorHandler.handleDestroyGenerator(block, player);
        event.setDropItems(false);

    }
}
