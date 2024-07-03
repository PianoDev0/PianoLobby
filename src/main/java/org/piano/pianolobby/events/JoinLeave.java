package org.piano.pianolobby.events;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.piano.pianolobby.system.PianoLobby;

public class JoinLeave implements Listener {
    private final PianoLobby plugin;
    private final LuckPerms luckPerms;

    public JoinLeave(PianoLobby plugin, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.luckPerms = luckPerms;
    }

    private boolean isFeatureEnabled(String feature) {
        return plugin.getConfig().getBoolean(feature, true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!isFeatureEnabled("player-join")) return;

        Player player = event.getPlayer();
        String joinMessage = plugin.getConfig().getString("join-message-format");

        if (luckPerms != null) {
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            if (user != null) {
                String prefix = user.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix();
                String playerName = player.getName();
                String coloredPrefix = prefix != null && !prefix.isEmpty() ? ChatColor.translateAlternateColorCodes('&', prefix) : "";
                String coloredPlayerName = ChatColor.WHITE + ChatColor.BOLD.toString() + playerName;
                String finalName = coloredPrefix + " " + coloredPlayerName;
                joinMessage = joinMessage.replace("{player}", finalName);
                joinMessage = ChatColor.translateAlternateColorCodes('&', joinMessage);
            }
        } else {
            joinMessage = joinMessage.replace("{player}", ChatColor.WHITE + ChatColor.BOLD.toString() + player.getName());
        }

        event.setJoinMessage(joinMessage);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!isFeatureEnabled("player-quit")) return;

        Player player = event.getPlayer();
        String quitMessage = plugin.getConfig().getString("quit-message-format");

        if (luckPerms != null) {
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            if (user != null) {
                String prefix = user.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix();
                String playerName = player.getName();
                String coloredPrefix = prefix != null && !prefix.isEmpty() ? ChatColor.translateAlternateColorCodes('&', prefix) : "";
                String coloredPlayerName = ChatColor.WHITE + ChatColor.BOLD.toString() + playerName;
                String finalName = coloredPrefix + " " + coloredPlayerName;
                quitMessage = quitMessage.replace("{player}", finalName);
                quitMessage = ChatColor.translateAlternateColorCodes('&', quitMessage);
            }
        } else {
            quitMessage = quitMessage.replace("{player}", ChatColor.WHITE + ChatColor.BOLD.toString() + player.getName());
        }

        event.setQuitMessage(quitMessage);
    }
}
