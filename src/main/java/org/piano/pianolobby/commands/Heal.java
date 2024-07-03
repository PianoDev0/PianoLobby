package org.piano.pianolobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.piano.pianolobby.system.PianoLobby;

public class Heal implements CommandExecutor {

    private final PianoLobby plugin;

    public Heal(PianoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("heal")) {
            Player target = null;

            if (sender instanceof Player) {
                Player p = (Player) sender;

                if (args.length == 1) {
                    String playerName = args[0];
                    target = Bukkit.getServer().getPlayerExact(playerName);

                    if (target == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("heal-command-player-not-online")));
                        return true;
                    }
                } else {
                    p.setHealth(p.getMaxHealth());
                    p.setFoodLevel(40);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("heal-command-self-success")));
                    return true;
                }
            } else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
                if (args.length == 1) {
                    String playerName = args[0];
                    target = Bukkit.getServer().getPlayerExact(playerName);

                    if (target == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("heal-command-player-not-online")));
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("heal-command-console-missing-argument")));
                    return true;
                }
            }

            if (target != null) {
                target.setHealth(target.getMaxHealth());
                target.setFoodLevel(30);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("heal-command-other-success").replace("%target%", target.getName())));
                return true;
            }
        }
        return false;
    }
}
