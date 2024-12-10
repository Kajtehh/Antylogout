package pl.kajteh.antylogout;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.kajteh.antylogout.config.CombatConfig;

import java.util.UUID;

public class CombatController implements Listener {

    private final CombatCache combatCache;
    private final CombatConfig combatConfig;

    public CombatController(CombatCache combatCache, CombatConfig combatConfig) {
        this.combatCache = combatCache;
        this.combatConfig = combatConfig;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        final Entity attacker = event.getDamager();
        final Entity victim = event.getEntity();

        if (!(attacker instanceof Player) && !(victim instanceof Player)) {
            return;
        }

        final UUID attackerUUID = attacker.getUniqueId();
        final UUID victimUUID = victim.getUniqueId();

        this.sendCombatStartMessage(attacker, victim);

        if (this.combatConfig.isCombatFromMobs() || victim instanceof Player) {
            this.combatCache.addCombat(attackerUUID, victimUUID);
        }

        if (this.combatConfig.isCombatFromMobs() || attacker instanceof Player) {
            this.combatCache.addCombat(victimUUID, attackerUUID);
        }
    }

    private void sendCombatStartMessage(Entity attacker, Entity victim) {
        if(!this.combatConfig.isCombatStartNotificationsEnabled()) {
            return;
        }

        if(attacker instanceof Player && !this.combatCache.getCombat(attacker.getUniqueId()).isPresent()) {
            this.combatConfig.getCombatStartMessageAttacker().send((Player) attacker, "victim", this.getEntityName(victim));
        }

        if(victim instanceof Player && !this.combatCache.getCombat(victim.getUniqueId()).isPresent()) {
            this.combatConfig.getCombatStartMessageVictim().send((Player) victim, "attacker", this.getEntityName(attacker));
        }
    }

    private String getEntityName(Entity entity) {
        if(entity instanceof Player) {
            return entity.getName();
        }

        if (entity instanceof LivingEntity) {
            final LivingEntity livingEntity = (LivingEntity) entity;

            return (livingEntity.getCustomName() != null ? livingEntity.getCustomName() : livingEntity.getType().name().toLowerCase()).trim();
        }

        return "Unknown";
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        this.handleCombatRemoval(event.getEntity(), true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        this.handleCombatRemoval(event.getEntity(), false);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        this.handleCombatRemoval(event.getEntity(), false);
    }

    private void handleCombatRemoval(Entity entity, boolean isPlayer) {
        if (!this.combatConfig.isRemoveCombatOnOpponentDeath()) {
            return;
        }

        final UUID entityId = entity.getUniqueId();

        this.combatCache.findCombatPlayerByOpponent(entityId).ifPresent(playerId -> {
            this.combatCache.removeCombat(playerId);

            final Player player = (Player) Bukkit.getOfflinePlayer(playerId);

            if (player == null || !player.isOnline()) {
                return;
            }

            this.combatConfig.getRemoveCombatMessage().send(player, "opponent", entity.getName());
        });

        if (isPlayer) {
            this.combatCache.removeCombat(entityId);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission(this.combatConfig.getCombatBypassPermission())) {
            player.setHealth(0);
        }

        this.combatCache.removeCombat(player.getUniqueId());
    }
}
