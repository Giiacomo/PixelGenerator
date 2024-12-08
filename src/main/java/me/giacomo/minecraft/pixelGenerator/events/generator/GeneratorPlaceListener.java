package me.giacomo.minecraft.pixelGenerator.events.generator;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorItem;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GeneratorPlaceListener implements Listener {
    @EventHandler
    public void onGeneratorPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            if (meta.hasDisplayName() && meta.getDisplayName().equals("§aItem Generator")) {
                Material generatedItem = Material.getMaterial(meta.getPersistentDataContainer().get(GeneratorItem.itemKey, PersistentDataType.STRING));
                Integer interval = meta.getPersistentDataContainer().get(GeneratorItem.intervalKey, PersistentDataType.INTEGER);
                Integer quantity = meta.getPersistentDataContainer().get(GeneratorItem.quantityKey, PersistentDataType.INTEGER);
                Utilities.informPlayer(event.getPlayer(), "You placed a" + "" + " generator");
                GeneratorBlock generator = new GeneratorBlock(event.getBlockPlaced(), generatedItem, interval, quantity);
                GeneratorManager.addGenerator(generator);
            }
        }
    }

    @EventHandler
    public void onGeneratorDestroy(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (GeneratorManager.isGenerator(block)) {
            Utilities.informPlayer(player, "You destroyed a generator!");

            GeneratorManager.removeGeneratorByBlock(block);
            event.setDropItems(false);

        }
    }
}
