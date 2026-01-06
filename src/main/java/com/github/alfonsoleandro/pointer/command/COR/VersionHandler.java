package com.github.alfonsoleandro.pointer.command.COR;

import com.github.alfonsoleandro.pointer.Pointer;
import com.github.alfonsoleandro.pointer.util.Message;
import org.bukkit.command.CommandSender;

public class VersionHandler extends AbstractHandler{

    public VersionHandler(Pointer plugin, AbstractHandler successor) {
        super(plugin, successor);
    }

    @Override
    protected boolean meetsCondition(CommandSender sender, String[] args) {
        return args.length > 0 && args[0].equalsIgnoreCase("version");
    }

    @Override
    protected void internalHandle(CommandSender sender, String label, String[] args) {
        if(!sender.hasPermission("Pointer.version")) {
            this.messageSender.send(sender, Message.NO_PERMISSION);
        }
    }

}
