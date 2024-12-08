package me.giacomo.minecraft.pixelGenerator.generators;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class GeneratorBlock implements Generator {

    private Block block;
    private Material itemToGenerate;
    private int quantity;
    private int interval;

    public GeneratorBlock(Block blockType, Material itemToGenerate, int quantity, int interval) {
        this.block = blockType;
        this.itemToGenerate = itemToGenerate;
        this.quantity = quantity;
        this.interval = interval;
    }


    @Override
    public void generateItem() {
        this.block.getWorld().dropItemNaturally(block.getLocation().add(0,1,0), new ItemStack(itemToGenerate, quantity));
    }

    @Override
    public void onPlayerInteract(Player player, Block block) {

    }

    @Override
    public int getInterval() {
        return interval;
    }

    @Override
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
}
