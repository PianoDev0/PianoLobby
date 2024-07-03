package org.piano.pianolobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.piano.pianolobby.system.PianoLobby;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Freeze implements CommandExecutor {

    private final Set<UUID> frozenPlayers = new HashSet<>();
    private final PianoLobby plugin;

    public Freeze(PianoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);

            if (target != null) {
                UUID targetUUID = target.getUniqueId();

                if (isPlayerFrozen(targetUUID)) {
                    unfreezePlayer(targetUUID);
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("freeze-unfrozen-message"))
                            .replace("{player}", target.getName()));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("freeze-unfrozen-message"))
                            .replace("{player}", target.getName()));
                } else {
                    freezePlayer(targetUUID);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("freeze-frozen-message"))
                            .replace("{player}", target.getName()));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("freeze-player-not-online")));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("freeze-usage-message")));
        }

        return true;
    }

    public boolean isPlayerFrozen(UUID playerUUID) {
        return frozenPlayers.contains(playerUUID);
    }

    public void freezePlayer(UUID playerUUID) {
        frozenPlayers.add(playerUUID);
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            player.setWalkSpeed(0);
        }
    }

    public void unfreezePlayer(UUID playerUUID) {
        frozenPlayers.remove(playerUUID);
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            player.setWalkSpeed(0.2f);
        }
    }
}

