package com.github.alfonsoleandro.pointer.command.COR;

import com.github.alfonsoleandro.pointer.Pointer;
import com.github.alfonsoleandro.pointer.util.Message;
import com.github.alfonsoleandro.pointer.util.Settings;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SetHandler extends AbstractHandler{

    private final Settings settings;

    public SetHandler(Pointer plugin, AbstractHandler successor) {
        super(plugin, successor);
        this.settings = plugin.getSettings();
    }

    @Override
    protected boolean meetsCondition(CommandSender sender, String[] args) {
        return args.length > 0  && args[0].equalsIgnoreCase("set");
    }

    @Override
    protected void internalHandle(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            this.messageSender.send(sender, Message.CANNOT_SEND_CONSOLE);
            return;
        }

        if (!player.hasPermission("Pointer.use")) {
            this.messageSender.send(sender, Message.NO_PERMISSION);
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.AIR || !itemInHand.hasItemMeta()) {
            this.messageSender.send(sender, Message.INVALID_ITEM);
            return;
        }

        ItemMeta itemMeta = itemInHand.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(this.settings.getPointerNamespacedKey(), PersistentDataType.BOOLEAN, true);
        itemInHand.setItemMeta(itemMeta);

        this.messageSender.send(sender, Message.POINTER_SET);
    }
}
