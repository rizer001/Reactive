package org.reactive.command;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public abstract class PermissionedReactiveSubcommand implements ReactiveSubcommand {

    private final Permission permission;

    protected PermissionedReactiveSubcommand(Permission permission) {
        this.permission = permission;
    }

    protected PermissionedReactiveSubcommand(String permission, PermissionDefault permissionDefault) {
        this(new Permission(permission, permissionDefault));
    }

    @Override
    public boolean testPermission(CommandSender sender) {
        return sender.hasPermission(this.permission);
    }

    @Override
    public Permission getPermission() {
        return this.permission;
    }
}
