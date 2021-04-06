package maku.survality.ids.util.tracker;

import maku.survality.ids.App;
import maku.survality.ids.mysql.MySQL;
import maku.survality.ids.util.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Track {

    public static int getID(String id) {
        try {
            PreparedStatement st = MySQL.con.prepareStatement("SELECT * FROM " + App.getInstance().getConfig().getString("mysqlTable") + " WHERE id = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return rs.getInt("id");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void track(Player player) {
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.sendMessage("§cChyba, v ruce nemáš item.");
            return;
        }
        if (player.getInventory().getItemInMainHand().getAmount() != 1) {
            player.sendMessage("§cSID lze zapnout pouze na jeden item najednou. Nepokoušej se ho stackovat.");
            return;
        }
        ItemMeta im = player.getItemInHand().getItemMeta();
        try {
            PreparedStatement st = MySQL.con.prepareStatement("INSERT INTO " + App.getInstance().getConfig().getString("mysqlTable") + " (id, nick, status) VALUES (NULL, ?, ?)");
            st.setString(1, player.getName());
            st.setString(2, "legal");
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Integer id = null;
        try {
            PreparedStatement st = MySQL.con.prepareStatement("SELECT * FROM " + App.getInstance().getConfig().getString("mysqlTable") + " WHERE nick = ?");
            st.setString(1, player.getName());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> lores = new ArrayList<String>();
        if (im.getLore() != null) {
            lores = (List<String>) im.getLore();
        }
        List<String> newLores = new ArrayList<String>();
        newLores.add(App.getMessages().getString("lore") + id);
        if (!lores.isEmpty()) {
            for (final String lore : lores) {
                if (!lore.startsWith(App.getMessages().getString("lore"))) {
                    newLores.add(lore);
                }
            }
        }
        im.setLore(newLores);
        player.getInventory().getItemInMainHand().setItemMeta(im);
        player.sendMessage("§aItem byl úspěšně vytvořen a bylo mu přiděleno SID: §2" + id);

        DiscordWebhook webhook = new DiscordWebhook(App.getWebhooks().getString("track"));
        webhook.setContent("Admin: `" + player.getName() + "` | SID: `" + id + "` | Item: `" + im.getDisplayName() + "`");
        Bukkit.getScheduler().runTaskAsynchronously(App.getInstance(), Runnable -> {
            try {
                webhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

}
