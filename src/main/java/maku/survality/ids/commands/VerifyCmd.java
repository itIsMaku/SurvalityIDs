package maku.survality.ids.commands;

import maku.survality.ids.App;
import maku.survality.ids.util.tracker.Status;
import maku.survality.ids.util.tracker.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VerifyCmd implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(App.getInstance().getConfig().getString("overitPermission"))) {
                if (args.length < 1) {
                    p.sendMessage("§cChyba, musíš zadat SID!");
                    return true;
                } else {
                    if (Track.getID(args[0]) == -1) {
                        p.sendMessage("§cItem s tímto SID neexistuje.");
                    } else {
                        if (Status.getStatus(args[0]).equalsIgnoreCase("legal")) {
                            p.sendMessage("§aSID §2" + Track.getID(args[0]) + "§a existuje, je přiděleno na itemu a je legální.");
                        } else {
                            p.sendMessage("§cSID §4" + Track.getID(args[0]) + "§c je nelegální!");
                        }
                    }
                }
            } else {
                p.sendMessage("§4§l[!] §cChyba, na toto nemáš dostatečná oprávnění!");
            }

        } else {
            System.out.println("§cTento prikaz muze pouzit pouze hrac.");
        }
        return false;
    }
}
