package me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.visibility;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class VisibilityManager {

    AbstractGeneratorBlock generatorBlock;

    public VisibilityManager(AbstractGeneratorBlock generatorBlock) {
        this.generatorBlock = generatorBlock;
    }

    public List<Player> getNearbyPlayers() {

        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getLocation().distance(generatorBlock.getBlock().getLocation()) < Ranges.VISIBILITY_RANGE.getRange())
                .map(player -> (Player) player)
                .collect(Collectors.toList());
    }

    public boolean isAnyoneInGenerationRange() {
        int blockChunkX = generatorBlock.getBlock().getX();
        int blockChunkZ = generatorBlock.getBlock().getZ();

        for (Player player : Bukkit.getOnlinePlayers()) {
            int playerX = player.getLocation().getBlockX();
            int playerZ = player.getLocation().getBlockZ();

            int generatorDistanceX = Math.abs(blockChunkX - playerX);
            int generatorDistanceZ = Math.abs(blockChunkZ - playerZ);

            if (generatorDistanceX <= Ranges.ACTIVATION_RANGE.getRange() && generatorDistanceZ <= Ranges.ACTIVATION_RANGE.getRange())
                return true;
        }
        return false;
    }

    public void playSoundToNearbyPlayers() {
        getNearbyPlayers().forEach(p -> {
            Utilities.playSound(generatorBlock.getSound(), p);
        });
    }

    public void startVisibilityUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!generatorBlock.getBlock().getChunk().isLoaded()) return;
                if (generatorBlock.getHologram() == null) return;
                getNearbyPlayers().forEach(player -> generatorBlock.getHologram().setShowPlayer(player));

                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> !getNearbyPlayers().contains(player))
                        .forEach(generatorBlock.getHologram()::removeShowPlayer);
            }
        }.runTaskTimer(PixelGenerator.getInstance(), 0L, 20L);
    }


}
