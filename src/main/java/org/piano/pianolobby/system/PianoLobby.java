package org.piano.pianolobby.system;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.piano.pianolobby.commands.*;
import org.piano.pianolobby.events.*;

public final class PianoLobby extends JavaPlugin {

    private static PianoLobby instance;
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getConsoleSender().sendMessage("[PianoLobby] Plugin byl správně načten!!");
        this.luckPerms = LuckPermsProvider.get();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Lobby(this, luckPerms), this);

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        getCommand("gmc").setExecutor(new Gamemodes(this));
        getCommand("gms").setExecutor(new Gamemodes(this));
        getCommand("gmsp").setExecutor(new Gamemodes(this));
        getCommand("fly").setExecutor(new Fly(this));
        getCommand("heal").setExecutor(new Heal(this));
        getCommand("flyspeed").setExecutor(new Fly(this));
        getCommand("die").setExecutor(new Other(this));
        getCommand("ping").setExecutor(new Other(this));
        getCommand("discord").setExecutor(new Other(this));
        getCommand("web").setExecutor(new Other(this));
        getCommand("about").setExecutor(new Other(this));
        getCommand("inventory").setExecutor(new Inventory(this));
        getCommand("day").setExecutor(new Time(this));
        getCommand("night").setExecutor(new Time(this));
        getCommand("sun").setExecutor(new Weather(this));
        getCommand("thunder").setExecutor(new Weather(this));
        getCommand("rain").setExecutor(new Weather(this));
        getCommand("freeze").setExecutor(new Freeze(this));
        getCommand("rules").setExecutor(new Rules(this));
        getCommand("pireload").setExecutor(new Reload(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static PianoLobby getInstance() {
        return instance;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
