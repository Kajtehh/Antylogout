package pl.kajteh.antylogout;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.kajteh.antylogout.config.CombatConfig;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class CombatRunnable implements Runnable {

    private final CombatCache combatCache;
    private final CombatConfig combatConfig;

    public CombatRunnable(CombatCache combatCache, CombatConfig combatConfig) {
        this.combatCache = combatCache;
        this.combatConfig = combatConfig;
    }

    @Override
    public void run() {
        final Instant now = Instant.now();

        for (UUID uuid : this.combatCache.getCombatPlayers()) {
            final Combat combat = this.combatCache.getCombat(uuid).orElse(null);

            if (combat == null) {
                continue;
            }

            final Player player = (Player) Bukkit.getOfflinePlayer(uuid);

            if (player == null || !player.isOnline()) {
                continue;
            }

            final long combatDuration = this.combatConfig.getCombatDuration();

            if (combat.hasElapsed(now, combatDuration)) {
                this.combatCache.removeCombat(uuid);
                this.combatConfig.getCombatEndMessages().forEach(message -> message.send(player));
                return;
            }

            final long timeLeft = combat.getTimeLeft(now, combatDuration);
            player.sendActionBar(String.format("Walka %ss", timeLeft));
        }
    }
}
