package org.piano.pianolobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.piano.pianolobby.system.PianoLobby;

public class Time implements CommandExecutor {

    private final PianoLobby plugin;

    public Time(PianoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((command.getName().equalsIgnoreCase("day") || command.getName().equalsIgnoreCase("night"))) {
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                Player p = (sender instanceof Player) ? (Player) sender : null;

                World world = (p != null) ? p.getWorld() : Bukkit.getWorlds().get(0);

                boolean isDayCommand = command.getName().equalsIgnoreCase("day");
                long time = isDayCommand ? 0 : 13000;
                world.setTime(time);

                String timeType = isDayCommand ? "day" : "night";

                if (p != null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("time-set-message-" + timeType)));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("time-set-message-" + timeType)));
                }

                return true;
            }
        }
        return false;
    }
}
