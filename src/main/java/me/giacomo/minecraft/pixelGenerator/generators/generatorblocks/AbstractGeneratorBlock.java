package me.giacomo.minecraft.pixelGenerator.generators.generatorblocks;


import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.helpers.GeneratorTaskScheduler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractGeneratorBlock<T> {

    protected static final float holoXOffset = 0.5f;
    protected static final float holoYOffset = 1.5f;
    protected static final float holoZOffset = 0.5f;

    protected Block block;
    protected T itemToGenerate;
    protected int quantity;
    protected int interval;

    protected BukkitTask task;
    protected Hologram hologram;

    public AbstractGeneratorBlock(Block blockType, T itemToGenerate, int interval, int quantity) {
        this.block = blockType;
        this.itemToGenerate = itemToGenerate;
        this.quantity = quantity;
        this.interval = interval;

        this.hologram = DHAPI.createHologram(getHoloName(), this.getBlock().getLocation().add(holoXOffset,holoYOffset, holoZOffset));
        this.updateHologramText();
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

    public void updateHologramText() {
        DHAPI.removeHologramLine(hologram, 0);
        DHAPI.addHologramLine(hologram, toString());
    }

    public abstract String getItemToGenerateFormatted();

    public String getDynamicHologramText(String s) {
        return "&f" + this.getQuantity() + " &f- &b" + this.getItemToGenerateFormatted() + " &f-&6 " + s + "&fs";
    }

    public void updateHologramText(String s) {
        //DHAPI.removeHologramLine(hologram, 0);
        DHAPI.setHologramLine(hologram, 0, getDynamicHologramText(s));
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
