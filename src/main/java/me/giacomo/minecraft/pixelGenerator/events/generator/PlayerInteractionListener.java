package me.giacomo.minecraft.pixelGenerator.events.generator;

import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import me.giacomo.minecraft.pixelGenerator.guis.windows.DefaultGUIWindow;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.Messages;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.Permissions;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractionListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (GeneratorManager.isGenerator(block)) {
            if (!Permissions.INTERACT.has(player)) {
                Utilities.warnPlayer(player, Messages.NO_PERMISSION.getValue());
                event.setCancelled(true);
                return;
            }
            if (event.getAction().isLeftClick())
                return;
            AbstractGeneratorBlock generator = GeneratorManager.findByBlock(block);

            new DefaultGUIWindow(player, generator);

            event.setCancelled(true);
        }
    }

}
