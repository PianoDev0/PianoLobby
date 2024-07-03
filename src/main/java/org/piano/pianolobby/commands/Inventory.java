package org.piano.pianolobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.piano.pianolobby.system.PianoLobby;

public class Inventory implements CommandExecutor {

    private final PianoLobby plugin;

    public Inventory(PianoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("inventory")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                if (args.length == 0) {
                    p.openInventory(p.getInventory());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("inventory-open-message-self")));
                } else if (args.length == 1) {
                    String playerName = args[0];
                    Player target = Bukkit.getServer().getPlayerExact(playerName);

                    if (target == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("inventory-player-not-online")));
                    } else {
                        p.openInventory(target.getInventory());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("inventory-open-message-other"))
                                .replace("{player}", target.getName()));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("inventory-usage-message")));
                }

                return true;
            }
        }

        return false;
    }
}

