package maku.survality.ids.commands;

import maku.survality.ids.App;
import maku.survality.ids.util.tracker.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrackCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.hasPermission(App.getInstance().getConfig().getString("pridelitPermission"))) {
                Track.track(p);
            } else {
                p.sendMessage("§4§l[!] §cChyba, na toto nemáš dostatečná oprávnění!");
            }
        } else {
            System.out.println("§cTento prikaz muze pouzit pouze hrac.");
        }
        return false;
    }
}
