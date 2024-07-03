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

public class Weather implements CommandExecutor {

    private final PianoLobby plugin;

    public Weather(PianoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((command.getName().equalsIgnoreCase("sun") || command.getName().equalsIgnoreCase("thunder") || command.getName().equalsIgnoreCase("rain"))) {
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                Player p = (sender instanceof Player) ? (Player) sender : null;

                World world = (p != null) ? p.getWorld() : Bukkit.getWorlds().get(0);

                boolean isSunCommand = command.getName().equalsIgnoreCase("sun");
                boolean isThunderCommand = command.getName().equalsIgnoreCase("thunder");
                boolean isRainCommand = command.getName().equalsIgnoreCase("rain");

                if (isSunCommand || isThunderCommand || isRainCommand) {
                    if (isThunderCommand) {
                        world.setStorm(!isSunCommand);
                        world.setThundering(!isSunCommand);
                    } else if (isRainCommand) {
                        world.setStorm(true);
                        world.setThundering(false);
                    } else { // isSunCommand
                        world.setStorm(false);
                        world.setThundering(false);
                    }

                    String weatherType = isSunCommand ? "sunny" : (isThunderCommand ? "stormy" : "rainy");

                    if (p != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("weather-set-message-" + weatherType)));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("weather-set-message-" + weatherType)));
                    }

                    return true;
                }
            }
        }
        return false;
    }
}


