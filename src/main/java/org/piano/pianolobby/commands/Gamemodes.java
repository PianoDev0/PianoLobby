package org.piano.pianolobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.piano.pianolobby.system.PianoLobby;

public class Gamemodes implements CommandExecutor {

    private final PianoLobby plugin;

    public Gamemodes(PianoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((command.getName().equalsIgnoreCase("gmc") || command.getName().equalsIgnoreCase("gms") || command.getName().equalsIgnoreCase("gmsp"))) {
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                Player p = (sender instanceof Player) ? (Player) sender : null;

                if (args.length == 0) {
                    GameMode targetGameMode;
                    if (command.getName().equalsIgnoreCase("gmc")) {
                        targetGameMode = GameMode.CREATIVE;
                    } else if (command.getName().equalsIgnoreCase("gms")) {
                        targetGameMode = GameMode.SURVIVAL;
                    } else {
                        targetGameMode = GameMode.SPECTATOR;
                    }

                    setGameMode(p, targetGameMode);

                    if (p != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gamemode-set-message"))
                                .replace("{player}", p.getName())
                                .replace("{gamemode}", targetGameMode.toString().toLowerCase()));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gamemode-set-message"))
                                .replace("{player}", "Console")
                                .replace("{gamemode}", targetGameMode.toString().toLowerCase()));
                    }

                    return true;
                } else if (args.length == 1) {
                    String playerName = args[0];
                    Player target = Bukkit.getServer().getPlayerExact(playerName);

                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "Tento hráč není online!");
                        return true;
                    }

                    GameMode targetGameMode;
                    if (command.getName().equalsIgnoreCase("gmc")) {
                        targetGameMode = GameMode.CREATIVE;
                    } else if (command.getName().equalsIgnoreCase("gms")) {
                        targetGameMode = GameMode.SURVIVAL;
                    } else {
                        targetGameMode = GameMode.SPECTATOR;
                    }

                    setGameMode(target, targetGameMode);

                    if (p != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gamemode-set-message-other"))
                                .replace("{player}", target.getName())
                                .replace("{gamemode}", targetGameMode.toString().toLowerCase()));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("gamemode-set-message-other"))
                                .replace("{player}", target.getName())
                                .replace("{gamemode}", targetGameMode.toString().toLowerCase()));
                    }

                    return true;
                }
            }
        }
        return false;
    }

    private void setGameMode(Player player, GameMode gameMode) {
        if (player != null) {
            player.setGameMode(gameMode);
        }
    }
}
