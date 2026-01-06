package com.github.alfonsoleandro.pointer;

import com.github.alfonsoleandro.mputils.files.YamlFile;
import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.github.alfonsoleandro.mputils.reloadable.ReloaderPlugin;
import com.github.alfonsoleandro.pointer.command.MainCommand;
import com.github.alfonsoleandro.pointer.listeners.PointerPointEvent;
import com.github.alfonsoleandro.pointer.util.Message;
import com.github.alfonsoleandro.pointer.util.Settings;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public final class Pointer extends ReloaderPlugin {

    private final PluginDescriptionFile pdfFile = getDescription();
    private final String version = this.pdfFile.getVersion();
    private Settings settings;
    private YamlFile configYaml;
    private YamlFile messagesYaml;
    private MessageSender<Message> messageSender;

    @Override
    public void onEnable() {
        registerFiles();
        this.messageSender = new MessageSender<>(this, Message.values(), this.messagesYaml, "prefix");
        this.settings = new Settings(this);
        this.messageSender.send("&aEnabled&f. Version: &e" + this.version);
        this.messageSender.send("&fThank you for using my plugin! &c" + this.pdfFile.getName() + "&f By " + this.pdfFile.getAuthors().getFirst());
        this.messageSender.send("&fJoin my discord server at &chttps://bit.ly/MPDiscordSv");
        this.messageSender.send("Please consider subscribing to my yt channel: &c" + this.pdfFile.getWebsite());
        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        this.messageSender.send("&cDisabled&f. Version: &e" + this.version);
        this.messageSender.send("&fThank you for using my plugin! &c" + this.pdfFile.getName() + "&f By " + this.pdfFile.getAuthors().getFirst());
        this.messageSender.send("&fJoin my discord server at &chttps://bit.ly/MPDiscordSv");
        this.messageSender.send("Please consider subscribing to my yt channel: &c" + this.pdfFile.getWebsite());
    }

    @Override
    public void reload(boolean deep) {
        registerFiles();
        super.reload(deep);
    }

    private void registerFiles() {
        this.configYaml = new YamlFile(this, "config.yml");
        this.messagesYaml = new YamlFile(this, "messages.yml");
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PointerPointEvent(this), this);
    }

    private void registerCommands() {
        PluginCommand mainCommand = getCommand("pointer");

        if (mainCommand == null) {
            this.messageSender.send("&cThe main command has not been registered properly. Disabling Pointer");
            this.setEnabled(false);
            return;
        }
        mainCommand.setExecutor(new MainCommand(this));
    }

    public YamlFile getConfigYaml() {
        return this.configYaml;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public MessageSender<Message> getMessageSender() {
        return this.messageSender;
    }
}
