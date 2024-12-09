package pl.kajteh.antylogout.api;

import pl.kajteh.antylogout.CombatCache;
import pl.kajteh.antylogout.config.CombatConfig;

public interface AntylogoutAPI {
    String getVersion();
    CombatCache getCombatCache();
    CombatConfig getCombatConfig();
}
