package me.giacomo.minecraft.pixelGenerator.generators.generatorblocks;


import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.visibility.VisibilityManager;
import me.giacomo.minecraft.pixelGenerator.helpers.GeneratorTaskScheduler;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.ActionSounds;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractGeneratorBlock {

    protected static final float holoXOffset = 0.5f;
    protected static final float holoYOffset = 1.85f;
    protected static final float holoZOffset = 0.5f;

    protected Block block;
    protected int quantity;
    protected int interval;

    protected BukkitTask task;
    protected Hologram hologram;

    protected ActionSounds sound;

    protected VisibilityManager visibility;

    public AbstractGeneratorBlock(Block blockType, int interval, int quantity) {
        this.block = blockType;
        this.quantity = quantity;
        this.interval = interval;

        this.sound = ActionSounds.GENERATOR_1;

        if (PixelGenerator.getInstance().getConfig().getBoolean("generator-holo.visible")){
            this.hologram = DHAPI.createHologram(getHoloName(), this.getBlock().getLocation().add(holoXOffset, holoYOffset, holoZOffset));
            this.hologram.setDefaultVisibleState(false);
            addHoloLines();
        }
        visibility = new VisibilityManager(this);

        visibility.startVisibilityUpdater();
    }

    public ActionSounds getSound() {
        return sound;
    }

    public void setSound(ActionSounds sound) {
        this.sound = sound;
    }


    public Hologram getHologram() {
        return hologram;
    }

    private void addHoloLines () {
        DHAPI.addHologramLine(hologram, "");
        DHAPI.addHologramLine(hologram, "");
    }


    public abstract String getItemToGenerateName();

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

    public abstract Material getItemToGenerateMaterial();

    public void updateHologramText(String s) {
        if (PixelGenerator.getInstance().getConfig().getBoolean("generator-holo.hologram-item"))  {
            DHAPI.setHologramLine(hologram, 1, getItemToGenerateMaterial());
            DHAPI.setHologramLine(hologram, 0,  "&b" + getQuantity() + " &f* &6"+ s + "&fs");
        } else {
            DHAPI.setHologramLine(hologram, 0, "&b" + getQuantity() + "&f* &b" + getItemToGenerateFormatted());
            DHAPI.setHologramLine(hologram, 1, "&6"+ s + "&fs");
        }
    }

    public GeneratorTaskScheduler getScheduleGenerationTask() {
        Runnable task = new Runnable() {
            protected long timeRemaining = 20L * getInterval();

            @Override
            public void run() {
                if (!block.getChunk().isLoaded()) {
                    PixelGenerator.getInstance().getServer().getScheduler().runTaskLater(
                            PixelGenerator.getInstance(),
                            this,
                            200L
                    );
                    return;
                }

                if (!visibility.isAnyoneInGenerationRange()) {
                    return;
                }

                if (timeRemaining > 0) {
                    timeRemaining -= 20;
                } else {
                    generateItem();
                    timeRemaining = 20L * getInterval();
                }
                if (hologram != null)
                    updateHologramText(String.valueOf(timeRemaining / 20));
            }
        };
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
