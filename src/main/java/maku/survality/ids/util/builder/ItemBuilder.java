package maku.survality.ids.util.builder;

import maku.survality.ids.App;
import maku.survality.ids.mysql.MySQL;
import maku.survality.ids.util.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**

 @author: itIsMaku

**/

public class ItemBuilder {

    private final ItemStack is;
    private final ItemMeta meta;

    public ItemBuilder(Material material, int amount) {
        is = new ItemStack(material, amount);
        meta = is.getItemMeta();
    }

    public ItemBuilder setDisplayNameName(String displayName) {
        meta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreak) {
        meta.setUnbreakable(unbreak);
        return this;
    }

    public ItemBuilder hideEnchants() {
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchant, int level) {
        meta.addEnchant(enchant, level, true);
        return this;
    }


    public ItemBuilder addLore(String lore) {
        List<String> lores = new ArrayList<>();
        if (meta.getLore() != null && meta.getLore().size() > 0) {
            lores = meta.getLore();
        }
        lores.add(lore);
        meta.setLore(lores);
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        meta.addEnchant(Enchantment.DURABILITY, 1, glow);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder track(Player player) {
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

        addLore(App.getMessages().getString("lore") + id);

        DiscordWebhook webhook = new DiscordWebhook(App.getWebhooks().getString("track"));
        webhook.setContent("Na hráče: `" + player.getName() + "` | SID: `" + id + "` | Item: `" + meta.getDisplayName() + "`");
        Bukkit.getScheduler().runTaskAsynchronously(App.getInstance(), Runnable -> {
            try {
                webhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return this;
    }

    public ItemStack build() {
        is.setItemMeta(meta);
        return is;
    }

}
