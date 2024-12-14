package me.giacomo.minecraft.pixelGenerator.guis.items;

import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.ActionSounds;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class SoundGUIItem extends AbstractItem {

    private static final ActionSounds[] generationSounds = {ActionSounds.GENERATOR_1, ActionSounds.GENERATOR_2, ActionSounds.GENERATOR_3, ActionSounds.GENERATOR_4};
    private final AbstractGeneratorBlock generatorBlock;

    public SoundGUIItem(AbstractGeneratorBlock generator) {
        this.generatorBlock = generator;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.NOTE_BLOCK).setDisplayName("Change generation sound");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        ActionSounds currentSound = generatorBlock.getSound();
        int nextIndex = (indexOf(currentSound) + 1) % generationSounds.length;
        ActionSounds nextSound = generationSounds[nextIndex];
        generatorBlock.setSound(nextSound);
        Utilities.informPlayer(player, "Sound changed to: " + nextSound.name());
    }

    private int indexOf(ActionSounds sound) {
        for (int i = 0; i < generationSounds.length; i++) {
            if (generationSounds[i] == sound) {
                return i;
            }
        }
        return -1;
    }
}
