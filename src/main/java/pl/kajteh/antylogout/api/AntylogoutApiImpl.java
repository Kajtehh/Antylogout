package pl.kajteh.antylogout.api;

import pl.kajteh.antylogout.CombatCache;
import pl.kajteh.antylogout.CombatPlugin;
import pl.kajteh.antylogout.config.CombatConfig;

public class AntylogoutApiImpl implements AntylogoutApi {

    private final CombatPlugin plugin;

    public AntylogoutApiImpl(CombatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public CombatCache getCombatCache() {
        return this.plugin.getCombatCache();
    }

    @Override
    public CombatConfig getCombatConfig() {
        return this.plugin.getCombatConfig();
    }
}
