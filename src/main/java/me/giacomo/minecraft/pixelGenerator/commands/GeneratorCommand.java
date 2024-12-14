package me.giacomo.minecraft.pixelGenerator.commands;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.exceptions.MaterialNotBlockException;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorItem;
import me.giacomo.minecraft.pixelGenerator.generators.GeneratorManager;
import me.giacomo.minecraft.pixelGenerator.helpers.enums.Messages;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GeneratorCommand implements CommandExecutor {

    private GeneratorManager generatorManager;

    public GeneratorCommand(GeneratorManager generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            Utilities.warnPlayer(sender, "Only players can use this command!");
            return true;
        }

        if (args.length == 0) {
            Utilities.warnPlayer(player, command.getUsage());
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> handleCreateCommand(player, args);
            case "list" -> handleListCommand(player);
            case "remove" -> handleRemoveCommand(player);
            case "reload" -> handleReloadCommand(player);
            case "clear" -> handleClearCommand(player, args);
            case "save" -> handleSaveCommand(player);
            case "debug" -> handleDebugCommand(player);
            default -> Utilities.warnPlayer(player, Messages.UNKNOWN_SUBCOMMAND.getValue() + ": " + args[0].toLowerCase());
        }

        return true;

        }


    private void handleCreateCommand(Player player, String[] args) {
        if (!player.hasPermission("pixelgenerator.create")) {
            sendNoPermission(player);
            return;
        }

        if (args.length < 5) {
            Utilities.warnPlayer(player, "Usage: /generator create <block> <material> <quantity> <interval>");
            return;
        }
        ItemStack generatorItem = null;

        try {
            String blockName = args[1].toUpperCase();
            String itemName = args[2].toUpperCase();
            int interval = Integer.parseInt(args[3]);
            int quantity = Integer.parseInt(args[4]);
            generatorItem = GeneratorItem.createGeneratorItem(blockName, itemName, interval, quantity);

        } catch (NumberFormatException e) {
            Utilities.warnPlayer(player, Messages.PARAMETER_INTEGER_EXCEPTION.getValue());
        } catch (MaterialNotBlockException | IllegalArgumentException e) {
            Utilities.warnPlayer(player, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Utilities.warnPlayer(player, Messages.DEFAULT_ERROR.getValue());
        }

        if (generatorItem == null) {
            return;
        }
        player.getInventory().addItem(generatorItem);
        Utilities.informPlayer(player, Messages.PLAYER_RECEIVE_GENERATOR.getValue());
    }


    private void handleListCommand(Player player) {
        if (!player.hasPermission("pixelgenerator.list")) {
            sendNoPermission(player);
            return;
        }

        //generatorManager.listGenerators(player);
    }

    private void handleRemoveCommand(Player player) {
        if (!player.hasPermission("pixelgenerator.remove")) {
            sendNoPermission(player);
            return;
        }

        //generatorManager.removeGenerator(player.getLocation().getBlock());
        //player.sendMessage(ChatColor.GREEN + "Generator removed!");
    }

    private void handleReloadCommand(Player player) {
        if (!player.hasPermission("pixelgenerator.admin.reload")) {
            sendNoPermission(player);
            return;
        }

        PixelGenerator.reload();
        Utilities.informPlayer(player,"Configuration reloaded.");
    }

    private void handleClearCommand(Player player, String[] args) {
        if (!player.hasPermission("pixelgenerator.admin.clear")) {
            sendNoPermission(player);
            return;
        }
        /*
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /generator clear <player>");
            return;
        }

        String targetPlayer = args[1];
        generatorManager.clearGeneratorsForPlayer(targetPlayer);
        player.sendMessage(ChatColor.GREEN + "Cleared generators for " + targetPlayer);*/
    }

    private void handleSaveCommand(Player player) {
        if (!player.hasPermission("pixelgenerator.admin.save")) {
            sendNoPermission(player);
            return;
        }

        //generatorManager.saveGenerators();
        //player.sendMessage(ChatColor.GREEN + "Generators saved.");
    }

    private void handleDebugCommand(Player player) {
        if (!player.hasPermission("pixelgenerator.admin.debug")) {
            sendNoPermission(player);
            return;
        }

        //generatorManager.toggleDebug();
        //player.sendMessage(ChatColor.GREEN + "Debug mode toggled.");
    }

    private void sendNoPermission(Player player) {
        Utilities.warnPlayer(player, Messages.NO_PERMISSION.getValue());
    }
}
