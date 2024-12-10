package pl.kajteh.antylogout;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import pl.kajteh.antylogout.api.AntylogoutAPI;
import pl.kajteh.antylogout.api.AntylogoutAPIImpl;
import pl.kajteh.antylogout.config.CombatConfig;
import pl.kajteh.antylogout.config.CombatConfigImpl;

public final class CombatPlugin extends JavaPlugin {

    private AntylogoutAPI api;
    private CombatCache combatCache;
    private CombatConfig combatConfig;

    @Override
    public void onEnable() {
        this.api = new AntylogoutAPIImpl(this);

        this.getServer().getServicesManager().register(AntylogoutAPI.class, this.api, this, ServicePriority.Normal);

        this.combatConfig = new CombatConfigImpl(this);

        this.combatCache = new CombatCache();

        this.getServer().getPluginManager().registerEvents(new CombatController(this.combatCache, this.combatConfig), this);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new CombatRunnable(this.combatCache, this.combatConfig), 20L, 20L);
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
