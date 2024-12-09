package pl.kajteh.antylogout.config;

import pl.kajteh.antylogout.message.Message;

import java.util.Set;

public interface CombatConfig {
    String getCombatBypassPermission();
    long getCombatDuration();
    Message getCombatMessage();
    Set<Message> getCombatEndMessages();
    boolean isCombatFromMobs();
    boolean isCombatFromProjectiles();
    Set<String> getCombatBlockedRegions();
}
