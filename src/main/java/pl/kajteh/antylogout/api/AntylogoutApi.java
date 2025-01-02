package pl.kajteh.antylogout.api;

import pl.kajteh.antylogout.CombatCache;
import pl.kajteh.antylogout.config.CombatConfig;

public interface AntylogoutApi {
    String getVersion();
    CombatCache getCombatCache();
    CombatConfig getCombatConfig();
}
