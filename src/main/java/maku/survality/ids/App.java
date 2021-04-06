package maku.survality.ids;

import maku.survality.ids.commands.ChangeStatusCommand;
import maku.survality.ids.commands.TrackCmd;
import maku.survality.ids.commands.VerifyCmd;
import maku.survality.ids.commands.generate.Adventurer;
import maku.survality.ids.listener.InventoryClickEvent;
import maku.survality.ids.mysql.MySQL;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class App extends JavaPlugin {

    private static App instance;

    private static final String consolePrefixEnable = "§a[TrackingSIDs] ";
    private static final String consolePrefixDisable = "§c[TrackingSIDs] ";

    //Webhooks
    private static final File webhooksyml = new File("plugins//TrackingIDs//webhooks.yml");
    private static final YamlConfiguration webhooks = YamlConfiguration.loadConfiguration(webhooksyml);

    //Messages
    private static final File messagesyml = new File("plugins//TrackingIDs//messages.yml");
    private static final YamlConfiguration messages = YamlConfiguration.loadConfiguration(messagesyml);

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        instance = this;
        System.out.println(consolePrefixEnable + "Enabling plugin...");
        System.out.println(consolePrefixEnable + "Version 1.0 - by itIsMaku");

        createFiles();

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new InventoryClickEvent(), this);

        getCommand("track").setExecutor(new TrackCmd());
        getCommand("verify").setExecutor(new VerifyCmd());
        getCommand("changestatus").setExecutor(new ChangeStatusCommand());
        getCommand("generateadventurer").setExecutor(new Adventurer());

        MySQL.connect();
    }

    @Override
    public void onDisable() {
        System.out.println(consolePrefixDisable + "Disabling plugin...");
        System.out.println(consolePrefixDisable + "Version 1.0 - by itIsMaku");
        MySQL.disconnect();
    }

    private void createFiles() {
        if (!webhooksyml.exists()) {
            try {
                webhooksyml.createNewFile();
                webhooks.set("track", (Object) "put your webhook here");
                webhooks.set("blocked-items", (Object) "put your webhook here");
                webhooks.save(webhooksyml);
                System.out.println(consolePrefixEnable + "File webhooks.yml created and setuped.");
            } catch (IOException exception) {
                System.out.println(consolePrefixDisable + "Oh, there s ain exception!");
                exception.printStackTrace();
            }
        }
        if (!messagesyml.exists()) {
            try {
                messagesyml.createNewFile();
                messages.set("lore", (Object) "§8SID: ");
                messages.save(messagesyml);
                System.out.println(consolePrefixEnable + "File messages.yml created and setuped.");
            } catch (IOException exception) {
                System.out.println(consolePrefixDisable + "Oh, there s ain exception!");
                exception.printStackTrace();
            }
        }
    }

    public static App getInstance() {
        return instance;
    }

    public static File getMessagesYml() {
        return messagesyml;
    }

    public static YamlConfiguration getMessages() {
        return messages;
    }

    public static File getWebhooksyml() {
        return webhooksyml;
    }

    public static YamlConfiguration getWebhooks() {
        return webhooks;
    }

}
