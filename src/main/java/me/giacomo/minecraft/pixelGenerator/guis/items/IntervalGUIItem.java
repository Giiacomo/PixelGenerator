package me.giacomo.minecraft.pixelGenerator.guis.items;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class IntervalGUIItem extends AbstractGUIItem {


    public IntervalGUIItem(AbstractGeneratorBlock generator) {
        super(generator);
    }

    @Override
    public ItemProvider getItemProvider() {
        String configItemStr = PixelGenerator.getInstance().getConfig().getString("gui.items.interval-item.item");
        String configItemName = PixelGenerator.getInstance().getConfig().getString("gui.items.interval-item.name");
        Material configItem = Material.getMaterial(configItemStr);
        if (!configItem.isItem())
            return new ItemBuilder(Material.BARRIER).setDisplayName("Invalid item in config! Interval: " + generator.getInterval());

        return new ItemBuilder(configItem).setDisplayName(configItemName + " (" + generator.getInterval() +")");
    }

    @Override
    protected void updateValue(int delta, Player player) {
        if (delta < 0 && generator.setInterval(generator.getInterval() + delta)) {
            Utilities.informPlayer(player, "Updated interval: " + generator.getInterval());
        } else if (delta > 0 && generator.setInterval(generator.getInterval() + delta)) {
            Utilities.informPlayer(player, "Updated interval: " + generator.getInterval());
        } else {
            Utilities.warnPlayer(player, "Interval can only be a positive number!");
        }
    }

    @Override
    protected String getValue() {
        return String.valueOf(generator.getInterval());
    }
}
