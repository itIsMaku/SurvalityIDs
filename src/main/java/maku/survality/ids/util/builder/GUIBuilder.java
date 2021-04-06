package maku.survality.ids.util.builder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**

 @author: itIsMaku

 **/

public class GUIBuilder {

    private Inventory gui;

    public GUIBuilder(Player player, int size, String name) {
        gui = Bukkit.createInventory(player, size, name);
    }

    public GUIBuilder setItem(int slot, ItemStack item) {
        gui.setItem(slot, item);
        return this;
    }

    public GUIBuilder setItem(int slot, ItemBuilder item) {
        return setItem(slot, item.build());
    }

    public GUIBuilder setPermissionItem(Player player, int slot, ItemBuilder item, String permission, ItemBuilder nopermission) {
        if(player.hasPermission(permission)) {
            setItem(slot, nopermission);
        } else {
            setItem(slot, item);
        }

        return this;
    }

    public GUIBuilder setNoPermissionItem(Player player, int slot, ItemBuilder item, String permission, ItemBuilder nopermission) {
        if(player.hasPermission(permission)) {
            setItem(slot, item);
        } else {
            setItem(slot, nopermission);
        }

        return this;
    }

    public GUIBuilder open(Player player) {
        player.openInventory(gui);
        return this;
    }

}
