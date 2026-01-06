package com.github.alfonsoleandro.pointer.util;

import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
import com.github.alfonsoleandro.mputils.sound.SoundSettings;
import com.github.alfonsoleandro.pointer.Pointer;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Settings extends Reloadable {

    private final Pointer plugin;
    private final MessageSender<Message> messageSender;
    private final NamespacedKey pointerNamespacedKey;
    private boolean trailEnabled;
    private boolean trailDynamicPoints;
    private int maxPointingDistance;
    private int trailPoints;
    private int trailSkipDistance;
    private SoundSettings pointSoundSettings;
    private Particle trailParticle;
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
        this.trailEnabled = config.getBoolean("particle trail.enabled");
        this.trailDynamicPoints = config.getBoolean("particle trail.dynamic points");
        this.trailPoints = config.getInt("particle trail.points");
        this.trailSkipDistance = config.getInt("particle trail.skip distance");
        String trailParticleName = config.getString("particle trail.particle");
        try {
            this.trailParticle = Particle.valueOf(trailParticleName);
        } catch (IllegalArgumentException | NullPointerException e) {
            this.messageSender.send(Bukkit.getConsoleSender(), "&cParticle name \"&f"+trailParticleName+"&c\" is an invalid particle name. Disabling particle trail.");
            this.trailEnabled = false;
        }
    }

    public NamespacedKey getPointerNamespacedKey() {
        return this.pointerNamespacedKey;
    }

    public boolean isTrailEnabled() {
        return this.trailEnabled;
    }

    public boolean isTrailDynamicPoints() {
        return this.trailDynamicPoints;
    }

    public int getMaxPointingDistance() {
        return this.maxPointingDistance;
    }

    public int getTrailPoints() {
        return this.trailPoints;
    }

    public int getTrailSkipDistance() {
        return this.trailSkipDistance;
    }

    public Particle getTrailParticle() {
        return this.trailParticle;
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
