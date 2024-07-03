package org.piano.pianolobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.piano.pianolobby.system.PianoLobby;

public class Fly implements CommandExecutor {

    private final PianoLobby plugin;

    public Fly(PianoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("fly")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length == 0) {
                    // Toggle fly mode for the player who issued the command
                    toggleFly(player, player);
                } else if (args.length == 1) {
                    String playerName = args[0];
                    Player target = Bukkit.getServer().getPlayerExact(playerName);

                    if (target == null) {
                        player.sendMessage(ChatColor.RED + "Tento hráč není online!");
                    } else {
                        // Toggle fly mode for the specified target player
                        toggleFly(player, target);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Použití: /fly [hráč]");
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Tento příkaz mohou použít pouze hráči!");
            }
        } else if (command.getName().equalsIgnoreCase("flyspeed")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length == 1) {
                    try {
                        float speed = Float.parseFloat(args[0]);
                        if (speed >= 0.1 && speed <= 10) {
                            player.setFlySpeed(speed / 10); // Fly speed is between 0.1 and 1.0
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("flyspeed-set-message"))
                                    .replace("{speed}", args[0]));
                        } else {
                            player.sendMessage(ChatColor.RED + "Zadejte platnou rychlost od 0.1 do 1!");
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Zadejte platnou rychlost od 0.1 do 1!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Použití: /flyspeed <rychlost>");
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Tento příkaz mohou použít pouze hráči!");
            }
        }
        return false;
    }

    private void toggleFly(Player sender, Player target) {
        if (target.getAllowFlight()) {
            target.setAllowFlight(false);
            target.setFlying(false);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("fly-off-message"))
                    .replace("{player}", target.getName()));
        } else {
            target.setAllowFlight(true);
            target.setFlying(true);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("fly-on-message"))
                    .replace("{player}", target.getName()));
        }
    }
}
