package me.giacomo.minecraft.pixelGenerator.generators.generatorblocks;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class CustomItemGeneratorBlock extends AbstractGeneratorBlock<ItemStack> {

    public CustomItemGeneratorBlock(Block blockType, ItemStack itemToGenerate, int interval, int quantity, ItemStack item) {
        super(blockType, itemToGenerate, interval, quantity);
    }

    @Override
    public String getItemToGenerateName() {
        return "";
    }

    @Override
    public String getItemToGenerateFormatted() {
        return "";
    }

    @Override
    public void generateItem() {

    }
}
