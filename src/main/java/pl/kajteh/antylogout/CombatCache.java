package pl.kajteh.antylogout;

import java.util.*;

public class CombatCache {

    private final Map<UUID, Combat> combatMap = new HashMap<>();

    public void addCombat(UUID player, UUID opponent) {
        this.combatMap.put(player, new Combat(opponent));
    }

    public void removeCombat(UUID player) {
        this.combatMap.remove(player);
    }

    public Optional<Combat> getCombat(UUID player) {
        return Optional.ofNullable(this.combatMap.get(player));
    }

    public Optional<UUID> findCombatPlayerByOpponent(UUID opponent) {
        return this.combatMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getOpponent().equals(opponent))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public Set<UUID> getCombatPlayers() {
        return this.combatMap.keySet();
    }
}
