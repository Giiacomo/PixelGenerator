package me.giacomo.minecraft.pixelGenerator.generators.generatorblocks;


import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.helpers.GeneratorTaskScheduler;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.ActionSounds;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;

public abstract class AbstractGeneratorBlock<T> {

    protected static final float holoXOffset = 0.5f;
    protected static final float holoYOffset = 1.75f;
    protected static final float holoZOffset = 0.5f;
    protected static final float VISIBILITY_RADIUS = 6f;

    protected Block block;
    protected T itemToGenerate;
    protected int quantity;
    protected int interval;

    protected BukkitTask task;
    protected Hologram hologram;

    protected ActionSounds sound;

    public AbstractGeneratorBlock(Block blockType, T itemToGenerate, int interval, int quantity) {
        this.block = blockType;
        this.itemToGenerate = itemToGenerate;
        this.quantity = quantity;
        this.interval = interval;

        this.sound = ActionSounds.GENERATOR_1;

        this.hologram = DHAPI.createHologram(getHoloName(), this.getBlock().getLocation().add(holoXOffset,holoYOffset, holoZOffset));
        this.hologram.setDefaultVisibleState(false); // Nascondi a tutti per default
        addHoloLines();

        startVisibilityUpdater();
    }

    public ActionSounds getSound() {
        return sound;
    }

    public void setSound(ActionSounds sound) {
        this.sound = sound;
    }

    public void playSound(Player player) {
        Utilities.playSound(sound, player);
    }

    public void playSoundToNearbyPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            double distance = player.getLocation().distance(block.getLocation());
            if (distance <= VISIBILITY_RADIUS) {
                playSound(player);
            }
        }
    }

    private void startVisibilityUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!block.getChunk().isLoaded()) return;
                if (hologram == null) return;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    double distance = player.getLocation().distance(block.getLocation());
                    if (distance <= VISIBILITY_RADIUS) {
                        hologram.setShowPlayer(player);
                    } else {
                        hologram.removeShowPlayer(player);
                    }
                }
            }
        }.runTaskTimer(PixelGenerator.getInstance(), 0L, 20L);
    }

    private void addHoloLines () {
        DHAPI.addHologramLine(hologram, "");
        DHAPI.addHologramLine(hologram, "");
    }

    public T getItemToGenerate() {
        return itemToGenerate;
    }

    public abstract String getItemToGenerateName();

    public void setItemToGenerate(T itemToGenerate) {
        this.itemToGenerate = itemToGenerate;
    }

    public String getHoloName () {
        return PixelGenerator.hologramPrefix + getBlock().getX() + "_x_" + getBlock().getY() + "_y_" + getBlock().getZ() + "_z";
    }

    public void updateHologramPosition () {
        DHAPI.moveHologram(hologram.getName(), this.getBlock().getLocation().add(holoXOffset, holoYOffset, holoZOffset));
    }

    public void removeHologram () {
        DHAPI.removeHologram(hologram.getName());
    }


    public String getItemToGenerateFormatted() {
        return getItemToGenerateName().replace("_", " ");
    }


    public void updateHologramText(String s) {
        DHAPI.setHologramLine(hologram, 0, "&b" + this.getQuantity() + " &f* &b" + this.getItemToGenerateFormatted());
        DHAPI.setHologramLine(hologram, 1, "&6"+ s + "&fs");
    }

    public GeneratorTaskScheduler getScheduleGenerationTask() {
        Runnable task = new Runnable() {
            protected long timeRemaining = 20L * getInterval();

            @Override
            public void run() {
                if (timeRemaining > 0) {
                    timeRemaining -= 20;
                } else {
                    generateItem();
                    timeRemaining = 20L * getInterval();
                }
                updateHologramText(String.valueOf(timeRemaining / 20));
            }
        };
        long interval = 20L * this.getInterval();
        long delay = interval;
        return new GeneratorTaskScheduler(task, 0L, 20L);
    }

    public void setTask(BukkitTask task) {
        cancelTask();
        this.task = task;
    }

    public void cancelTask() {
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    public abstract void generateItem();

    public void onPlayerInteract(Player player, Block block) {
        return;
    }

    public int getInterval() {
        return interval;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean setQuantity(int quantity) {
        if (quantity <= 0) {
            this.quantity = 1;
            return false;
        }
        this.quantity = quantity;
        return true;
    }

    public boolean setInterval(int interval) {
        if (interval <= 0) {
            interval = 1;
            return false;
        }
        this.interval = interval;
        return true;
    }
}
