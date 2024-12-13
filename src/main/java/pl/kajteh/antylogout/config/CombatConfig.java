package pl.kajteh.antylogout.config;

import pl.kajteh.antylogout.message.Message;

import java.util.Set;

public interface CombatConfig {
    String getCombatBypassPermission();
    long getCombatDuration();

    boolean isCombatFromMobs();
    boolean isCombatFromProjectiles();

    Message getCombatMessage();
    Set<Message> getCombatEndMessages();

    /* TODO
    boolean isCombatBossBarEnabled();
    CombatBossBar getCombatBossBar();
    CombatBossBar getCombatEndBossBar();
     */

    boolean isCombatStartNotificationsEnabled();
    Message getCombatStartMessageAttacker();
    Message getCombatStartMessageVictim();

    boolean isRemoveCombatOnOpponentDeath();
    Set<Message> getRemoveCombatMessage();

    boolean isCommandsBlockedDuringCombat();
    Set<String> getCombatCommandWhitelist();
    Set<Message> getCombatCommandBlockedMessage();

    Set<String> getCombatBlockedRegions();
    double getCombatBlockedRegionKnockbackMultiplier();
    Set<Message> getCombatBlockedRegionEnterMessage();

    void save();
    void reload();
}
