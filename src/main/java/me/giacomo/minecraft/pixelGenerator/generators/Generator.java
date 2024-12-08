package me.giacomo.minecraft.pixelGenerator.generators;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface Generator {
    void generateItem();

    void onPlayerInteract(Player player, Block block);

    int getInterval();

    Block getBlock();
}
