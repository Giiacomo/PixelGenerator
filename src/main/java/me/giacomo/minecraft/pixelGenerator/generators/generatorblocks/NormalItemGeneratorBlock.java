package me.giacomo.minecraft.pixelGenerator.generators.generatorblocks;


import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class NormalItemGeneratorBlock extends AbstractGeneratorBlock<Material> {


    public NormalItemGeneratorBlock(Block block, Material itemToGenerate, int interval, int quantity) {
        super(block, itemToGenerate, interval, quantity);
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
    public void generateItem() {
        this.block.getWorld().dropItem(block.getLocation().add(0.5,1,0.5), new ItemStack(itemToGenerate, quantity));
        playSoundToNearbyPlayers();
    }
}
