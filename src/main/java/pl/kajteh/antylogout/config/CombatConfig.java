package pl.kajteh.antylogout.config;

import pl.kajteh.antylogout.message.Message;

import java.util.Set;

public interface CombatConfig {
    String getCombatBypassPermission();
    long getCombatDuration();
    boolean isCombatStartNotificationsEnabled();
    Message getCombatStartMessageAttacker();
    Message getCombatStartMessageVictim();
    Message getCombatMessage();
    Set<Message> getCombatEndMessages();
    boolean isRemoveCombatOnOpponentDeath();
    Message getRemoveCombatMessage();
    boolean isCombatFromMobs();
    boolean isCombatFromProjectiles(); // TODO
    boolean isCommandsBlockedDuringCombat();
    Set<String> getCombatCommandWhitelist();
    Message getCombatCommandBlockedMessage();
    Set<String> getCombatBlockedRegions(); // TODO
}
