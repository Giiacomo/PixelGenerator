package me.giacomo.minecraft.pixelGenerator.generators.generatorblocks;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.generators.items.GenerableItemManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CustomItemGeneratorBlock extends AbstractGeneratorBlock {

    private ItemStack itemToGenerate;

    public CustomItemGeneratorBlock(Block blockType, ItemStack itemToGenerate, int interval, int quantity) {
        super(blockType, interval, quantity);
    }

    public ItemStack getItemToGenerate() {
        return itemToGenerate;
    }

    public void setItemToGenerate(ItemStack itemToGenerate) {
        this.itemToGenerate = itemToGenerate;
    }

    @Override
    public String getItemToGenerateName() {
        return GenerableItemManager.getKey(itemToGenerate);
    }

    @Override
    public String getItemToGenerateFormatted() {
        return itemToGenerate.getItemMeta().getDisplayName().replace("_", " ");
    }

    @Override
    public Material getItemToGenerateMaterial() {
        return itemToGenerate.getType();
    }

    @Override
    public void generateItem() {
        World world = block.getWorld();
        ItemStack itemToDrop = itemToGenerate.clone();
        itemToDrop.setAmount(quantity);
        if (PixelGenerator.getInstance().getConfig().getBoolean("generator-drop.on-place"))
            world.dropItem(block.getLocation().add(0.5,1,0.5), itemToDrop).setVelocity(new Vector(0, 0, 0));
        else
            world.dropItemNaturally(block.getLocation().add(0.5,1,0.5), itemToDrop);
        visibility.playSoundToNearbyPlayers();
    }
}
