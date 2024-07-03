package org.piano.pianolobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.piano.pianolobby.system.PianoLobby;

public class Other implements CommandExecutor {

    private final PianoLobby plugin;

    public Other(PianoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName().toLowerCase()) {
            case "die":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.setHealth(0.0);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfig().getString("die-command-success")));
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return false;
                }

            case "discord":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfig().getString("discord-command-message")));
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return false;
                }

            case "about":
                String aboutMessage = "§1§lABOUT PIANOLOBBY: \n"
                        + "§BVERSION: 1.0 \n"
                        + "§3PLUGIN BY: PianoDev_";
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.sendMessage(aboutMessage);
                    return true;
                } else if (sender instanceof ConsoleCommandSender) {
                    ConsoleCommandSender console = (ConsoleCommandSender) sender;
                    console.sendMessage(aboutMessage);
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "This command is not available.");
                    return false;
                }

            case "web":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfig().getString("web-command-message")));
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return false;
                }

            default:
                return false;
        }
    }
}
