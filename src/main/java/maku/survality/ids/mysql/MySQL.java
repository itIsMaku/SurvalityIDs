package maku.survality.ids.mysql;

import maku.survality.ids.App;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    public static Connection con;

    public static void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + App.getInstance().getConfig().getString("mysqlIP") + ":" + App.getInstance().getConfig().getString("mysqlPort") + "/" + App.getInstance().getConfig().getString("mysqlDB") + "?autoReconnect=true", App.getInstance().getConfig().getString("mysqlUser"), App.getInstance().getConfig().getString("mysqlPassword"));
                System.out.println("§aDatabaze pripojena.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                System.out.println("§aDatabaze odpojena.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        return (con != null);
    }

}
