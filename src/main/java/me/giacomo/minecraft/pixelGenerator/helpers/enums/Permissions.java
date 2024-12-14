package me.giacomo.minecraft.pixelGenerator.helpers.enums;

import me.giacomo.minecraft.pixelGenerator.PixelGenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public enum Permissions {
    ADMIN_CREATE("pixelgenerator.admin.create"),
    PLACE("pixelgenerator.place"),
    INTERACT("pixelgenerator.interact"),
    BREAK("pixelgenerator.break"),
    ADMIN_LIST("pixelgenerator.admin.list"),
    ADMIN_DEBUG("pixelgenerator.admin.debug"),
    ADMIN_ALL("pixelgenerator.admin.*");

    private final String permissionKey;

    Permissions(String permissionKey) {
        this.permissionKey = permissionKey;
    }

    public String getPermissionKey() {
        return permissionKey;
    }

    public boolean has(Player player) {
        return player.isOp() || player.hasPermission(permissionKey);
    }

    public Permission getPermission() {
        return Bukkit.getPluginManager().getPermission(permissionKey);
    }
}