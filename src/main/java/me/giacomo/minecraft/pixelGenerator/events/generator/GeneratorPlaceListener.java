package me.giacomo.minecraft.pixelGenerator.events.generator;

import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorItem;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GeneratorPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(PlayerInteractEvent event) {
        if (event.getAction().toString().contains("RIGHT_CLICK")) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                if (meta.hasDisplayName() && meta.getDisplayName().equals("§aItem Generator")) {
                    Block block = event.getClickedBlock();
                    Material material = Material.DIAMOND;
                    if (block != null) {
                        block.setType(Material.DIAMOND_BLOCK);
                        player.sendMessage("You placed the generator!");
                        player.sendMessage(String.valueOf(
                                player.getInventory().getItemInMainHand()
                                    .getItemMeta().getPersistentDataContainer()
                                        .get(GeneratorItem.intervalKey, PersistentDataType.INTEGER)));
                        GeneratorBlock generator = new GeneratorBlock(block, Material.DIAMOND, 1, 5);
                        GeneratorManager.addGenerator(generator);
                    }
                }
            }
        }
    }
}
