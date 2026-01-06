package com.github.alfonsoleandro.pointer.listeners;

import com.github.alfonsoleandro.mputils.sound.SoundSettings;
import com.github.alfonsoleandro.pointer.Pointer;
import com.github.alfonsoleandro.pointer.util.Settings;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

public class PointerPointEvent implements Listener {

    private final Pointer plugin;
    private final Settings settings;

    public PointerPointEvent(Pointer plugin) {
        this.plugin = plugin;
        this.settings = plugin.getSettings();
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("Pointer.use")) {
            return;
        }

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        // Only trigger for the main hand
        if (event.getHand() == null || !event.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }

        if (itemInMainHand.getType() == Material.AIR || !itemInMainHand.hasItemMeta()) {
            return;
        }

        PersistentDataContainer persistentDataContainer = Objects.requireNonNull(itemInMainHand.getItemMeta()).getPersistentDataContainer();
        if (!persistentDataContainer.has(this.settings.getPointerNamespacedKey(), PersistentDataType.BOOLEAN)
                || Boolean.FALSE.equals(persistentDataContainer.get(this.settings.getPointerNamespacedKey(), PersistentDataType.BOOLEAN))) {
            return;
        }

        Block block = player.getTargetBlock(this.settings.getTransparents(), this.settings.getMaxPointingDistance());
        if (this.settings.getTransparents().contains(block.getType())) {
            return;
        }
        highlightBlock(block);
        playSound(player);
        drawParticleLine(player.getLocation().add(0, 1.5, 0), block.getLocation().add(0.5, 0.5, 0.5));
    }

    private void highlightBlock(Block block) {
        Location loc = block.getLocation();
        BlockDisplay display = (BlockDisplay) block.getWorld().spawnEntity(
                loc,
                EntityType.BLOCK_DISPLAY
        );
        display.setBlock(block.getBlockData());
        display.setGlowing(true);

        // Slightly bigger so it outlines the block
        display.setTransformation(new Transformation(
                new Vector3f(-0.01f, -0.01f, -0.01f),
                new Quaternionf(),
                new Vector3f(1.02f, 1.02f, 1.02f),
                new Quaternionf()
        ));


        new BukkitRunnable() {

            @Override
            public void run() {
                display.remove();
            }

        }.runTaskLater(this.plugin, 40L);
    }

    private void playSound(Player player) {
        SoundSettings pointSoundSettings = this.settings.getPointSoundSettings();
        player.getWorld().playSound(player.getLocation(),
                pointSoundSettings.getSound(),
                SoundCategory.AMBIENT,
                pointSoundSettings.getVolume(),
                pointSoundSettings.getPitch());
    }

    public void drawParticleLine(Location start, Location end) {
        if (!this.settings.isTrailEnabled()) {
            return;
        }
        World world = start.getWorld();

        Vector direction = end.toVector().subtract(start.toVector());
        double length = direction.length();

        int skipDistance = this.settings.getTrailSkipDistance();
        if (length <= skipDistance) return;

        direction.normalize();

        int points = this.settings.isTrailDynamicPoints() ? (int) length : this.settings.getTrailPoints();

        Location beamStart = start.clone().add(direction.clone().multiply(skipDistance));
        double beamLength = length - skipDistance;

        double spacing = beamLength / points;

        Particle particle = this.settings.getTrailParticle();

        for (int i = 0; i < points; i++) {
            Location point = beamStart.clone().add(direction.clone().multiply(spacing * i));
            world.spawnParticle(
                    particle,
                    point,
                    1,
                    0, 0, 0,
                    0
            );
        }
    }
}
