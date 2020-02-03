package dev.genbyte.sunfright;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import dev.genbyte.sunfright.events.HelmetHandler;

public class Sunfright extends JavaPlugin {
    private Logger logger;
    private BukkitTask damager;

    @Override
    public void onLoad() {
        logger = this.getLogger();
    }

    @Override
    public void onEnable() {
        int time = 20;
        damager = new Damager(this).runTaskTimerAsynchronously(this, time, time);
        logger.log(Level.INFO, "Damager task started. and will run every " + (time / 20) + " seconds");

        this.getServer().getPluginManager().registerEvents(new HelmetHandler(), this);
    }

    @Override
    public void onDisable() {
        damager.cancel();
        logger.log(Level.INFO, "Damager task stopped.");
    }
}