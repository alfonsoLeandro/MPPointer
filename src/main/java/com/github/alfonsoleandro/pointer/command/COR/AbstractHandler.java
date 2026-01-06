package com.github.alfonsoleandro.pointer.command.COR;

import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.github.alfonsoleandro.pointer.Pointer;
import com.github.alfonsoleandro.pointer.util.Message;
import org.bukkit.command.CommandSender;

public abstract class AbstractHandler {

    protected final Pointer plugin;
    protected final MessageSender<Message> messageSender;
    protected final AbstractHandler successor;

    public AbstractHandler(Pointer plugin, AbstractHandler successor){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        this.successor = successor;
    }

    public void handle(CommandSender sender, String label, String[] args){
        if(meetsCondition(sender, args)){
            this.internalHandle(sender, label, args);
        }else{
            if(this.successor != null) this.successor.handle(sender, label, args);
            else this.messageSender.send(sender, Message.UNKNOWN_COMMAND,
                    "%command%", label);
        }
    }

    protected abstract boolean meetsCondition(CommandSender sender, String[] args);

    protected abstract void internalHandle(CommandSender sender, String label, String[] args);

}
