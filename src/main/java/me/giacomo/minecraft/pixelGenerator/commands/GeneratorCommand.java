package me.giacomo.minecraft.pixelGenerator.commands;

import jdk.jshell.execution.Util;
import me.giacomo.minecraft.pixelGenerator.exceptions.MaterialNotBlockException;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorBlock;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorItem;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class GeneratorCommand implements CommandExecutor {

    private GeneratorManager generatorManager;

    public GeneratorCommand(GeneratorManager generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;
        if (args.length != 4) {
            Utilities.warnPlayer(sender, command.getUsage());
            return true;
        }
        ItemStack generatorItem = null;

        try {
            String blockName = args[0];
            String itemName = args[1];
            int interval = Integer.parseInt(args[2]);
            int quantity = Integer.parseInt(args[3]);
            generatorItem = GeneratorItem.createGeneratorItem(blockName, itemName, interval, quantity);

        } catch (NumberFormatException e) {
            Utilities.warnPlayer(sender, "Last two parameters need to be integer numbers");
        } catch (MaterialNotBlockException | IllegalArgumentException e) {
            Utilities.warnPlayer(sender, e.getMessage());
        } catch (Exception e) {
            Utilities.warnPlayer(sender, "Something went wrong");
        }

        if (generatorItem == null) {
            return true;
        }

        player.getInventory().addItem(generatorItem);
        Utilities.informPlayer(sender, "You have received a generator item");

        return true;

    }
}
