package com.github.alfonsoleandro.pointer.command.COR;

import com.github.alfonsoleandro.pointer.Pointer;
import org.bukkit.command.CommandSender;

public class HelpHandler extends AbstractHandler{

    public HelpHandler(Pointer plugin, AbstractHandler successor) {
        super(plugin, successor);
    }

    @Override
    protected boolean meetsCondition(CommandSender sender, String[] args) {
        return args.length == 0 || args[0].equalsIgnoreCase("help");
    }

    @Override
    protected void internalHandle(CommandSender sender, String label, String[] args) {
        this.messageSender.send(sender, "&6List of commands");
        this.messageSender.send(sender, "&f/"+label+" help");
        this.messageSender.send(sender, "&f/"+label+" reload");
        this.messageSender.send(sender, "&f/"+label+" version");
        this.messageSender.send(sender, "&f/"+label+" set");
        this.messageSender.send(sender, "&f/"+label+" remove");

    }
}
