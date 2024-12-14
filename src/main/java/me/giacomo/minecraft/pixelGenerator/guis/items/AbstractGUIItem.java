package me.giacomo.minecraft.pixelGenerator.guis.items;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.sql.SQLException;

public abstract class AbstractGUIItem extends AbstractItem {
        protected final AbstractGeneratorBlock generator;

        public AbstractGUIItem(AbstractGeneratorBlock generator) {
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
            generator.setTask(generator.getScheduleGenerationTask().schedule());
            notifyWindows();
        }

        protected abstract void updateValue(int delta, Player player);

        protected abstract String getValue();
}

