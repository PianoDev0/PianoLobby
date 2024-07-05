package org.piano.pianolobby.events;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.piano.pianolobby.system.PianoLobby;

public class Lobby implements Listener {

    private final PianoLobby plugin;

    public Lobby(PianoLobby plugin) {
        this.plugin = plugin;
    }

    private boolean isFeatureEnabled(String feature) {
        return plugin.getConfig().getBoolean(feature, true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!isFeatureEnabled("entity-damage")) return;

        if (event.getEntity() instanceof Player damaged && event.getDamager() instanceof Player damager) {
            if (damager.isOp() || damager.hasPermission("nohit.bypass")) {
                return;
            }

            if (damaged.isOp() || damaged.hasPermission("nohit.bypass")) {
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!isFeatureEnabled("fall-damage")) return;

        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (isFeatureEnabled("lobby-effects")) {
            setUnlimitedEffects(player);
        }

        setDayTime();
    }

    private void setUnlimitedEffects(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 4, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, true, false));
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isFeatureEnabled("block-break")) return;

        if (event.getPlayer().hasPermission("nobreak.bypass")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!isFeatureEnabled("block-place")) return;

        if (event.getPlayer().hasPermission("nobuild.bypass")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityBlockChange(EntityChangeBlockEvent event) {
        if (!isFeatureEnabled("entity-block-change")) return;

        if (event.getEntity() instanceof Player player && player.hasPermission("nobreak.bypass")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!isFeatureEnabled("player-drop-item")) return;

        if (event.getPlayer().hasPermission("noitem.bypass")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        if (!isFeatureEnabled("player-pickup-item")) return;

        if (event.getPlayer().hasPermission("noitem.bypass")) {
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
            if (event.getPlayer() instanceof Player player && player.hasPermission("nobreak.bypass")) {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!isFeatureEnabled("pl-command-block")) return;

        String command = event.getMessage().toLowerCase();
        if (command.equalsIgnoreCase("/bukkit:plugins") || command.equalsIgnoreCase("/bukkit:pl") || command.equalsIgnoreCase("/plugins") || command.equalsIgnoreCase("/pl")) {
            if (!event.getPlayer().isOp()) {
                event.getPlayer().sendMessage(ChatColor.WHITE + "Plugins(3):" + ChatColor.GREEN + " Not, Your, Business");
                event.setCancelled(true);
            }
        }
    }

    public void startTask() {
        if (!isFeatureEnabled("day-time")) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                setDayTime();
            }
        }.runTaskTimer(plugin, 0L, 24000L);
    }

    private void setDayTime() {
        for (World world : Bukkit.getWorlds()) {
            world.setTime(1000L);
        }
    }
}
