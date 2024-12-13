package pl.kajteh.antylogout;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import pl.kajteh.antylogout.api.AntylogoutAPI;
import pl.kajteh.antylogout.api.AntylogoutAPIImpl;
import pl.kajteh.antylogout.config.CombatConfig;
import pl.kajteh.antylogout.config.CombatConfigImpl;
import pl.kajteh.antylogout.controller.CombatRegionController;
import pl.kajteh.antylogout.controller.CombatController;

import java.util.Objects;

public final class CombatPlugin extends JavaPlugin {

    private static final int BSTATS_PLUGIN_ID = 24122;

    private AntylogoutAPI api;
    private CombatCache combatCache;
    private CombatConfig combatConfig;

    @Override
    public void onEnable() {
        this.api = new AntylogoutAPIImpl(this);

        this.getServer().getServicesManager().register(AntylogoutAPI.class, this.api, this, ServicePriority.Normal);

        this.combatConfig = new CombatConfigImpl(this);
        this.combatCache = new CombatCache();

        final long taskInterval = 20L;

        this.getServer().getScheduler()
                .runTaskTimerAsynchronously(this, new CombatRunnable(this.combatCache, this.combatConfig), taskInterval, taskInterval);

        this.getServer().getPluginManager().registerEvents(new CombatController(this.combatCache, this.combatConfig), this);

        if(this.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            this.getServer().getPluginManager()
                    .registerEvents(new CombatRegionController(this.combatCache, this.combatConfig), this);
        }

        Objects.requireNonNull(this.getCommand("antylogout")).setExecutor(new CombatCommand(this.combatConfig, this.combatCache));

        new Metrics(this, BSTATS_PLUGIN_ID);
    }

    public CombatCache getCombatCache() {
        return this.combatCache;
    }

    public CombatConfig getCombatConfig() {
        return this.combatConfig;
    }

    public AntylogoutAPI getApi() {
        return this.api;
    }
}
