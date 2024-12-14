package me.giacomo.minecraft.pixelGenerator.guis.windows;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.db.GeneratorDB;
import me.giacomo.minecraft.pixelGenerator.generators.generatorblocks.AbstractGeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.guis.items.IntervalGUIItem;
import me.giacomo.minecraft.pixelGenerator.guis.items.QuantityGUIItem;
import me.giacomo.minecraft.pixelGenerator.guis.items.SoundGUIItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.sql.SQLException;

public class DefaultGUIWindow {

    public DefaultGUIWindow(Player player, AbstractGeneratorBlock generator) {
        String title = PixelGenerator.getInstance().getConfig().getString("gui.window.name");
        if (title == null)
            title = "Generator GUI";

        Gui gui = Gui.normal() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                        "# # # # # # # # #",
                        "# . I . S . Q . #",
                        "# # # # # # # # #"
                )
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)))
                .addIngredient('I', new IntervalGUIItem(generator))
                .addIngredient('Q', new QuantityGUIItem(generator))
                .addIngredient('S', new SoundGUIItem(generator))
                .build();
        Window window = Window.single()
                .setViewer(player)
                .setTitle(title)
                .setGui(gui)
                .addCloseHandler(() -> {
                    GeneratorDB db = PixelGenerator.getInstance().getGeneratorDB();
                    try {
                        db.updateGeneratorParameters(generator);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();

        window.open();

    }
}