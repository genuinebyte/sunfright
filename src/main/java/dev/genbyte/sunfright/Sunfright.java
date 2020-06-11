package dev.genbyte.sunfright;

import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import dev.genbyte.sunfright.events.HelmetHandler;

public class Sunfright extends JavaPlugin {
    public World sunnedWorld;
    public int damagaPerSecond;
    private BukkitTask damager;

    @Override
    public void onEnable() {
        if (!loadConfig()) {
            getLogger().log(Level.SEVERE, "Could not load config file, aborting load. Plugin not active!");
            getPluginLoader().disablePlugin(this);
            return;
        }

        int time = 20;
        damager = new Damager(this).runTaskTimerAsynchronously(this, time, time);

        this.getServer().getPluginManager().registerEvents(new HelmetHandler(this), this);
    }

    @Override
    public void onDisable() {
        damager.cancel();
    }

    private boolean loadConfig() {
        this.saveDefaultConfig();

        String world = getConfig().getString("world");
        damagaPerSecond = getConfig().getInt("damagePerSecond");

        sunnedWorld = getServer().getWorld(world);
        if (sunnedWorld == null) {
            getLogger().log(Level.SEVERE, "World '" + world + "' is not valid! Aborting loading config!");
            return false;
        }

        if (damagaPerSecond == 0) {
            getLogger().log(Level.WARNING, "damagePerSecond is 0. Was this intended?");
        }

        return true;
    }
}