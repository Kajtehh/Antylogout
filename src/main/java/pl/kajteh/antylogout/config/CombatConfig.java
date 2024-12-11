package pl.kajteh.antylogout.config;

import pl.kajteh.antylogout.message.Message;

import java.util.Set;

public interface CombatConfig {
    String getCombatBypassPermission();
    long getCombatDuration();

    boolean isCombatFromMobs();
    boolean isCombatFromProjectiles(); // TODO

    Message getCombatMessage();
    Set<Message> getCombatEndMessages();

    boolean isCombatStartNotificationsEnabled();
    Message getCombatStartMessageAttacker();
    Message getCombatStartMessageVictim();

    boolean isRemoveCombatOnOpponentDeath();
    Message getRemoveCombatMessage();

    boolean isCommandsBlockedDuringCombat();
    Set<String> getCombatCommandWhitelist();
    Message getCombatCommandBlockedMessage();

    Set<String> getCombatBlockedRegions(); // TODO
    double getCombatBlockedRegionKnockbackMultiplier();
    Message getCombatBlockedRegionEnterMessage();
}
