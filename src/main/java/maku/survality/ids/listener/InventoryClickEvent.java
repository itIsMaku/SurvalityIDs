package maku.survality.ids.listener;

import maku.survality.ids.App;
import maku.survality.ids.util.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Objects;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public void invClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            Player p = (Player) e.getWhoClicked();
            ItemStack clicked = e.getCurrentItem();

            if (!clicked.hasItemMeta() || !Objects.requireNonNull(clicked.getItemMeta()).hasDisplayName()) {
                return;
            }
            for (String itemName : App.getInstance().getConfig().getStringList("blockeditems")) {
                if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(itemName)) {
                    p.sendMessage("§cByl ti zabaven item s názvem §4" + itemName + "§c.");
                    e.getClickedInventory().remove(clicked);

                    DiscordWebhook webhook = new DiscordWebhook(App.getWebhooks().getString("blocked-items"));
                    webhook.setContent("Hráči " + p.getName() + " byl zabaven item s názvem **" + itemName + "** (" + clicked.getType().toString() + ")!");
                    Bukkit.getScheduler().runTaskAsynchronously(App.getInstance(), Runnable -> {
                        try {
                            webhook.execute();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    });
                }
            }

        } else {
            return;
        }
    }

}
