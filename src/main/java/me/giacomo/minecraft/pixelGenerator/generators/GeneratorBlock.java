package me.giacomo.minecraft.pixelGenerator.generators;


import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.helpers.GeneratorTaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitTask;

import java.util.Vector;

public class GeneratorBlock {

    private Block block;
    private Material itemToGenerate;
    private int quantity;
    private int interval;
    private BukkitTask task;
    private Hologram hologram;

    public GeneratorBlock(Block blockType, Material itemToGenerate, int interval, int quantity) {
        this.block = blockType;
        this.itemToGenerate = itemToGenerate;
        this.quantity = quantity;
        this.interval = interval;
        this.hologram = DHAPI.createHologram(getHoloName(), this.getBlock().getLocation().add(0.5,1.5,0.5));
        this.updateHologram();
    }

    public String getHoloName () {
        return getBlock().getX() + "_x_" + getBlock().getY() + "_y_" + getBlock().getZ() + "_z";
    }

    public void updateHologram() {
        //DHAPI.moveHologram(hologram.getName(), hologram.getLocation());
        DHAPI.removeHologramLine(hologram, 0);
        DHAPI.addHologramLine(hologram, toString());
    }

    public GeneratorTaskScheduler getScheduleGenerationTask() {
        Runnable task = this::generateItem;
        long interval = 20L * this.getInterval();
        long delay = interval;
        return new GeneratorTaskScheduler(task, delay, interval);
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

    public void generateItem() {
        this.block.getWorld().dropItem(block.getLocation().add(0.5,1,0.5), new ItemStack(itemToGenerate, quantity));
    }

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

    public Material getItemToGenerate() {
        return itemToGenerate;
    }

    public void setItemToGenerate(Material itemToGenerate) {
        this.itemToGenerate = itemToGenerate;
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

    @Override
    public String toString() {
        return "&b" + this.getItemToGenerate().toString() + " &f*&b " + this.getQuantity() + " &f*&b " + this.getInterval() + "&fs";
    }
}
