package me.giacomo.minecraft.pixelGenerator.generators.generatorblocks;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class CustomItemGeneratorBlock extends AbstractGeneratorBlock<ItemStack> {

    public CustomItemGeneratorBlock(Block blockType, ItemStack itemToGenerate, int interval, int quantity) {
        super(blockType, itemToGenerate, interval, quantity);
    }

    @Override
    public String getItemToGenerateName() {
        return itemToGenerate.getItemMeta().getLore().getLast();
    }

    @Override
    public String getItemToGenerateFormatted() {
        return itemToGenerate.getItemMeta().getDisplayName().replace("_", " ");
    }

    @Override
    public void generateItem() {
        World world = block.getWorld();
        ItemStack itemToDrop = itemToGenerate.clone();
        itemToDrop.setAmount(quantity);
        world.dropItemNaturally(block.getLocation(), itemToDrop);
    }
}
