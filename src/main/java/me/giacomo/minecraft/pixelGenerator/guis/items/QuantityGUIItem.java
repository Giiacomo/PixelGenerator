package me.giacomo.minecraft.pixelGenerator.guis.items;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class QuantityGUIItem extends AbstractGUIItem {

    public QuantityGUIItem(GeneratorBlock generator) {
        super(generator);
    }

    @Override
    public ItemProvider getItemProvider() {
        String configItemStr = PixelGenerator.getInstance().getConfig().getString("gui.items.quantity-item.item");
        String configItemName = PixelGenerator.getInstance().getConfig().getString("gui.items.quantity-item.name");
        Material configItem = Material.getMaterial(configItemStr);
        if (!configItem.isItem())
            return new ItemBuilder(Material.BARRIER).setDisplayName("Invalid item in config! Quantity: " + generator.getQuantity());

        return new ItemBuilder(configItem).setDisplayName(configItemName + " (" + generator.getQuantity() +")");
    }

    @Override
    protected void updateValue(int delta, Player player) {
        if (delta < 0 && generator.setQuantity(generator.getQuantity() + delta)) {
            Utilities.informPlayer(player, "Updated quantity: " + generator.getQuantity());
        } else if (delta > 0 && generator.setQuantity(generator.getQuantity() + delta)) {
            Utilities.informPlayer(player, "Updated quantity: " + generator.getQuantity());
        } else {
            Utilities.warnPlayer(player, "Quantity can only be a positive number!");
        }
    }

    @Override
    protected String getValue() {
        return String.valueOf(generator.getQuantity());
    }
}
