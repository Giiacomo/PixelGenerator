package me.giacomo.minecraft.pixelGenerator.guis.items;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class IntervalGUIItem extends AbstractItem {

    private final GeneratorBlock generator;
    private int interval;

    public IntervalGUIItem(GeneratorBlock generator) {
        this.generator = generator;
        interval = generator.getInterval();
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
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            if (clickType.isShiftClick()) {
                if (generator.setInterval(generator.getInterval() - 5))
                    Utilities.informPlayer(player, "Updated interval: " + generator.getInterval());
                else Utilities.warnPlayer(player, "Interval can only be a positive number!");
            } else {
                if (generator.setInterval(generator.getInterval() - 1))
                    Utilities.informPlayer(player, "Updated interval: " + generator.getInterval());
                else Utilities.warnPlayer(player, "Interval can only be a positive number!");            }
        } else {
            if (clickType.isShiftClick()) {
                generator.setInterval(generator.getInterval() + 5);
                Utilities.informPlayer(player, "Updated interval: " + generator.getInterval());
            } else {
                generator.setInterval(generator.getInterval() + 1);
                Utilities.informPlayer(player, "Updated interval: " + generator.getInterval());
            }
        }
        generator.setTask(generator.getScheduleGenerationTask().schedule());

        notifyWindows();
    }


}
