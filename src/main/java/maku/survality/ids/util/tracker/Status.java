package maku.survality.ids.util.tracker;

import maku.survality.ids.App;
import maku.survality.ids.mysql.MySQL;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Status {

    public static String getStatus(String id) {
        try {
            PreparedStatement st = MySQL.con.prepareStatement("SELECT status FROM " + App.getInstance().getConfig().getString("mysqlTable") + " WHERE id = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return rs.getString("status");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void change(Player player, String id) {
        if(getStatus(id).equalsIgnoreCase("legal")) {
            try {
                PreparedStatement st = MySQL.con.prepareStatement("UPDATE " + App.getInstance().getConfig().getString("mysqlTable") + " SET status = 'illegal' WHERE id = ?");
                st.setString(1, id);
                st.executeUpdate();

                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            player.sendMessage("§cSID §4" + id + " §cbylo nyní nastaveno na nelegální.");
        } else {
            try {
                PreparedStatement st = MySQL.con.prepareStatement("UPDATE " + App.getInstance().getConfig().getString("mysqlTable") + " SET status = 'legal' WHERE id = ?");
                st.setString(1, id);
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            player.sendMessage("§aSID §2" + id + " §abylo nyní nastaveno na legální.");
        }

    }

}
