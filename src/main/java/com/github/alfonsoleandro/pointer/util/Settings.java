package com.github.alfonsoleandro.pointer.util;

import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
import com.github.alfonsoleandro.mputils.sound.SoundSettings;
import com.github.alfonsoleandro.pointer.Pointer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Settings extends Reloadable {

    private final Pointer plugin;
    private final MessageSender<Message> messageSender;
    private final NamespacedKey pointerNamespacedKey;
    private int maxPointingDistance;
    private SoundSettings pointSoundSettings;
    private Set<Material> transparents;

    public Settings(Pointer plugin) {
        super(plugin, Priority.HIGHEST);
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        this.pointerNamespacedKey = new NamespacedKey(plugin, "pointer");
        loadFields();
    }

    private void loadFields() {
        FileConfiguration config = this.plugin.getConfigYaml().getAccess();
        this.maxPointingDistance = config.getInt("max pointing distance");
        this.pointSoundSettings = new SoundSettings(
                Objects.requireNonNull(config.getString("sound.name")),
                config.getDouble("sound.volume"),
                config.getDouble("sound.pitch"));
        this.transparents = new HashSet<>();
        for (String transparent : config.getStringList("transparent blocks")) {
            Material transparentMaterial = Material.getMaterial(transparent);
            if (transparentMaterial == null) {
                this.messageSender.send(Bukkit.getConsoleSender(), "&cMaterial \"&f"+transparent+"&c\" is an invalid material in the list of transparent materials");
                continue;
            }
            this.transparents.add(transparentMaterial);
        }
    }

    public NamespacedKey getPointerNamespacedKey() {
        return this.pointerNamespacedKey;
    }

    public int getMaxPointingDistance() {
        return this.maxPointingDistance;
    }

    public SoundSettings getPointSoundSettings() {
        return this.pointSoundSettings;
    }

    public Set<Material> getTransparents() {
        return this.transparents;
    }

    @Override
    public void reload(boolean deep) {
        loadFields();
    }
}
