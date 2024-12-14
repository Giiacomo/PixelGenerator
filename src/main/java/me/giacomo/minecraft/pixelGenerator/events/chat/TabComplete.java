package me.giacomo.minecraft.pixelGenerator.events.chat;


import me.giacomo.minecraft.pixelGenerator.generators.items.GenerableItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class TabComplete implements TabCompleter {


    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;

        ArrayList<String> arguments = new ArrayList<>();

        if (command.getName().equals("generator")) {

            // no arguments
            if (args.length == 1){
                if (player.hasPermission("pixelgenerator.admin.*")) { arguments.addAll(Arrays.asList("create", "list", "remove", "reload", "clear", "save", "debug")); }

                Iterator<String> iter = arguments.iterator(); while (iter.hasNext()) { String str = iter.next().toLowerCase(); if (!str.contains(args[0].toLowerCase())) iter.remove(); }
            }
            if (args.length == 2 && args[0].equals("create")) {
                arguments.addAll(GenerableItemManager.getAllItemNames());

                Iterator<String> iter = arguments.iterator(); while (iter.hasNext()) { String str = iter.next().toLowerCase(); if (!str.contains(args[1].toLowerCase())) iter.remove(); }
            }
        }

        return arguments;
    }

}