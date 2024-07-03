package org.piano.pianolobby.events;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.piano.pianolobby.system.PianoLobby;

public class Lobby implements Listener {

    private final PianoLobby plugin;
    private final LuckPerms luckPerms;

    public Lobby(PianoLobby plugin, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.luckPerms = luckPerms;
    }

    private boolean isFeatureEnabled(String feature) {
        return plugin.getConfig().getBoolean(feature, true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!isFeatureEnabled("entity-damage")) return;

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            if (damager.isOp() || damager.hasPermission("nohit.bypass")) {
                return;
            }

            if (damaged.isOp() || damaged.hasPermission("nohit.bypass")) {
                event.setCancelled(true);
                return;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!isFeatureEnabled("fall-damage")) return;

        if (event.getEntity() instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!isFeatureEnabled("player-join")) return;

        Player player = e.getPlayer();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        String joinMessage = plugin.getConfig().getString("join-message-format");



        if (user != null) {
            String prefix = user.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix();
            String playerName = player.getName();
            String coloredPrefix = prefix != null && !prefix.isEmpty() ? ChatColor.translateAlternateColorCodes('&', prefix) : "";
            String coloredPlayerName = ChatColor.WHITE + ChatColor.BOLD.toString() + playerName;
            String finalName = coloredPrefix + " " + coloredPlayerName;
            joinMessage = joinMessage.replace("{player}", finalName);
            joinMessage = ChatColor.translateAlternateColorCodes('&', joinMessage);
        }
        e.setJoinMessage(joinMessage);
        if (!isFeatureEnabled("lobby-effects")) return;
        setUnlimitedEffects(player);
    }

    private void setUnlimitedEffects(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 4, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, true, false));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (!isFeatureEnabled("player-quit")) return;

        Player player = e.getPlayer();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        String quitMessage = plugin.getConfig().getString("quit-message-format");
        if (user != null) {
            String prefix = user.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix();
            String playerName = player.getName();
            String coloredPrefix = prefix != null && !prefix.isEmpty() ? ChatColor.translateAlternateColorCodes('&', prefix) : "";
            String coloredPlayerName = ChatColor.WHITE + ChatColor.BOLD.toString() + playerName;
            String finalName = coloredPrefix + " " + coloredPlayerName;
            quitMessage = quitMessage.replace("{player}", finalName);
            quitMessage = ChatColor.translateAlternateColorCodes('&', quitMessage);
        }
        e.setQuitMessage(quitMessage);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isFeatureEnabled("block-break")) return;

        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("nobreak.bypass")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!isFeatureEnabled("block-place")) return;

        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("nobuild.bypass")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityBlockChange(EntityChangeBlockEvent event) {
        if (!isFeatureEnabled("entity-block-change")) return;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.isOp() || player.hasPermission("nobreak.bypass")) {
                return;
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!isFeatureEnabled("player-drop-item")) return;

        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("noitem.bypass")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        if (!isFeatureEnabled("player-pickup-item")) return;

        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("noitem.bypass")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!isFeatureEnabled("inventory-open")) return;

        InventoryHolder holder = event.getInventory().getHolder();
        Inventory inventory = event.getInventory();

        if (inventory.getType().toString().contains("CHEST") || inventory.getType().toString().contains("ENDER_CHEST")) {
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();

                if (player.isOp() || player.hasPermission("nobreak.bypass")) {
                    return;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!isFeatureEnabled("pl-command-block")) return;

        String command = event.getMessage().toLowerCase();
        if (command.equals("/bukkit:plugins") || command.equals("/bukkit:pl") || command.equals("/plugins") || command.equals("/pl")){
            if (!event.getPlayer().isOp()) {
                event.getPlayer().sendMessage("§fPlugins(3): §aNot, Your, Business");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!isFeatureEnabled("player-join")) return;

        setDayTime();
    }

    public void startTask() {
        if (!isFeatureEnabled("day-time")) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                setDayTime();
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("PianoLobby"), 0L, 24000L);
    }

    private void setDayTime() {
        for (World world : Bukkit.getWorlds()) {
            world.setTime(1000L);
        }
    }
}
