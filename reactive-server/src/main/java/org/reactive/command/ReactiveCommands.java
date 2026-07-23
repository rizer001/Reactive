package org.reactive.command;

import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.util.permissions.CraftDefaultPermissions;

import java.util.HashMap;
import java.util.Map;

public final class ReactiveCommands {

    public static final String COMMAND_BASE_PERM = CraftDefaultPermissions.LEAF_ROOT + ".command";

    private ReactiveCommands() {
    }

    private static final Map<String, Command> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put(ReactiveCommand.COMMAND_LABEL, new ReactiveCommand());
    }

    public static void registerCommands(final MinecraftServer server) {
        COMMANDS.forEach((s, command) -> server.server.getCommandMap().register(s, "Leaf", command));
    }
}
