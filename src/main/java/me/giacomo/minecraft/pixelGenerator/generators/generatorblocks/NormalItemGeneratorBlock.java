package me.giacomo.minecraft.pixelGenerator.generators.generatorblocks;


import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class NormalItemGeneratorBlock extends AbstractGeneratorBlock {

    private Material itemToGenerate;

    public NormalItemGeneratorBlock(Block block, Material itemToGenerate, int interval, int quantity) {
        super(block, interval, quantity);
        this.itemToGenerate = itemToGenerate;
    }

    public Material getItemToGenerate() {
        return itemToGenerate;
    }

    public void setItemToGenerate(Material itemToGenerate) {
        this.itemToGenerate = itemToGenerate;
    }

    @Override
    public String getItemToGenerateName() {
        return itemToGenerate.name();
    }

    @Override
    public String getItemToGenerateFormatted() {
        return getItemToGenerateName().replace("_", " ");
    }

    @Override
    public Material getItemToGenerateMaterial() {
        return itemToGenerate;
    }

    @Override
    public void generateItem() {
        if (PixelGenerator.getInstance().getConfig().getBoolean("generator-drop.on-place"))
            this.block.getWorld().dropItem(block.getLocation().add(0.5,1,0.5), new ItemStack(itemToGenerate, quantity)).setVelocity(new Vector(0,0,0));
        else
            this.block.getWorld().dropItemNaturally(block.getLocation().add(0.5,1,0.5), new ItemStack(itemToGenerate, quantity));

        visibility.playSoundToNearbyPlayers();
    }
}
