package com.github.alfonsoleandro.pointer.command;


import com.github.alfonsoleandro.pointer.Pointer;
import com.github.alfonsoleandro.pointer.command.COR.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private final AbstractHandler COR;

    public MainCommand(Pointer plugin) {
        this.COR = new HelpHandler(plugin, new VersionHandler(plugin, new SetHandler(plugin, new ReloadHandler(plugin, new RemoveHandler(plugin, null)))));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        this.COR.handle(sender, label, args);
        return true;
    }
}