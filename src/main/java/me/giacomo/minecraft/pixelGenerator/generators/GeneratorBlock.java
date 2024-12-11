package me.giacomo.minecraft.pixelGenerator.generators;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitTask;

public class GeneratorBlock {

    private Block block;
    private Material itemToGenerate;
    private int quantity;
    private int interval;
    private BukkitTask task;

    public GeneratorBlock(Block blockType, Material itemToGenerate, int interval, int quantity) {
        this.block = blockType;
        this.itemToGenerate = itemToGenerate;
        this.quantity = quantity;
        this.interval = interval;
    }
    public void setTask(BukkitTask task) {
        this.task = task;
    }

    public void cancelTask() {
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    public void generateItem() {
        this.block.getWorld().dropItemNaturally(block.getLocation().add(0,1,0), new ItemStack(itemToGenerate, quantity));
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "GeneratorBlock{" +
                "interval=" + interval +
                ", quantity=" + quantity +
                ", itemToGenerate=" + itemToGenerate +
                ", block=" + block +
                '}';
    }
}
