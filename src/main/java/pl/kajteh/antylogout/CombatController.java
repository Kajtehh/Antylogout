package pl.kajteh.antylogout;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatController implements Listener {

    private final CombatCache combatCache;

    public CombatController(CombatCache combatCache) {
        this.combatCache = combatCache;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        final Entity attacker = event.getDamager();
        final Entity victim = event.getEntity();

        if(attacker instanceof Player) {
            this.combatCache.addCombat(attacker.getUniqueId(), victim.getUniqueId());
        }

        if(victim instanceof Player) {
            this.combatCache.addCombat(victim.getUniqueId(), attacker.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.combatCache.removeCombat(player.getUniqueId());
    }
}
