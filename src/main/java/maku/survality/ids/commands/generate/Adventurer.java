package maku.survality.ids.commands.generate;

import maku.survality.ids.util.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Adventurer implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                System.out.println("Hrac s touto prezdivkou neni na serveru.");
                return true;
            }
            ItemBuilder hero = new ItemBuilder(Material.ORANGE_STAINED_GLASS, 1)
                    .setDisplayNameName("§eKompletní balíček §6§lADVENTURER")
                    .track(target)
                    .setGlow(true)
                    .addLore("§7Pro aktivaci klikni pravým.")
                    .addLore("§7Balíček zahrnuje všechny výhody.");

            target.getInventory().addItem(hero.build());
        } else {
            Player p = (Player) sender;
            p.sendMessage("§cTento příkaz lze využít pouze z konzole.");
        }
        return false;
    }
}
