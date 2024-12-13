package me.giacomo.minecraft.pixelGenerator.guis.items;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public abstract class AbstractGUIItem extends AbstractItem {
        protected final GeneratorBlock generator;

        public AbstractGUIItem(GeneratorBlock generator) {
            this.generator = generator;
        }

        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
            if (clickType.isLeftClick()) {
                if (clickType.isShiftClick()) {
                    updateValue(-5, player);
                } else {
                    updateValue(-1, player);
                }
            } else {
                if (clickType.isShiftClick()) {
                    updateValue(5, player);
                } else {
                    updateValue(1, player);
                }
            }
            generator.updateHologram();
            generator.setTask(generator.getScheduleGenerationTask().schedule());
            notifyWindows();
        }

        protected abstract void updateValue(int delta, Player player);

        protected abstract String getValue();
}

