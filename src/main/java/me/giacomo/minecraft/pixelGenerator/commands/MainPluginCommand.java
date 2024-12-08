package me.giacomo.minecraft.pixelGenerator.commands;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import me.giacomo.minecraft.pixelGenerator.helpers.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainPluginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("template.user")) {
            Utilities.warnPlayer(sender, PixelGenerator.getPhrase("no-permissions-message"));
            return true;
        }

        try {

            switch (args[0].toLowerCase()) {
                case "help":
                    help(sender);
                    break;
                case "info":
                    info(sender);
                    break;

                case "reload":
                    if (sender.hasPermission("template.admin")) reload(sender);
                    else Utilities.warnPlayer(sender, PixelGenerator.getPhrase("no-permissions-message"));
                    break;
                default:
                    Utilities.warnPlayer(sender, PixelGenerator.getPhrase("not-a-command-message"));
                    help(sender);
                    break;
            }

        } catch(Exception e) {
            Utilities.warnPlayer(sender, PixelGenerator.getPhrase("formatting-error-message"));
        }

        return true;
    }

    private void info(CommandSender sender) {
        sender.sendMessage(PixelGenerator.prefix + ChatColor.GRAY + "Plugin Info");

    }

    private void help(CommandSender sender) {
        sender.sendMessage(PixelGenerator.prefix + ChatColor.GRAY + "Commands");

    }

    private void reload(CommandSender sender) {
        PixelGenerator.getInstance().reloadConfig();
        PixelGenerator.getInstance().loadConfiguration();
        PixelGenerator.getInstance().loadLangFile();

        Utilities.informPlayer(sender, "configuration, values, and language settings reloaded");
    }

}