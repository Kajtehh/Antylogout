package pl.kajteh.antylogout.controller;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.kajteh.antylogout.CombatCache;
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
        final Entity victim = event.getEntity();

        Entity attacker = event.getDamager();

        if (this.combatConfig.isCombatFromProjectiles() && attacker instanceof Projectile) {
            final Projectile projectile = (Projectile) attacker;

            if (projectile.getShooter() instanceof Player) {
                attacker = (Entity) projectile.getShooter();
            } else if(this.combatConfig.isCombatFromMobs() && projectile.getShooter() instanceof LivingEntity) {
                attacker = (Entity) projectile.getShooter();
            }
        }

        this.handleCombat(attacker, victim);
    }

    private void handleCombat(Entity attacker, Entity victim) {
        if (!(attacker instanceof Player) && !(victim instanceof Player)) {
            return;
        }

        this.sendCombatStartMessage(attacker, victim);

        if (this.combatConfig.isCombatFromMobs()) {
            if (attacker instanceof Player) {
                this.startCombat(attacker, victim);
            }

            if (victim instanceof Player) {
                this.startCombat(victim, attacker);
            }
            return;
        }

        if (attacker instanceof Player && victim instanceof Player) {
            this.startCombat(attacker, victim);
            this.startCombat(victim, attacker);
        }
    }


    private void startCombat(Entity attacker, Entity victim) {
        final UUID attackerUUID = attacker.getUniqueId();
        final UUID victimUUID = victim.getUniqueId();

        this.combatCache.addCombat(attackerUUID, victimUUID, this.combatConfig.getCombatDuration());
    }

    private void sendCombatStartMessage(Entity attacker, Entity victim) {
        if(!this.combatConfig.isCombatStartNotificationsEnabled()) {
            return;
        }

        if(!(attacker instanceof Player) || !(victim instanceof Player)) {
            return;
        }

        if(!this.combatCache.getCombat(attacker.getUniqueId()).isPresent()) {
            this.combatConfig.getCombatStartMessageAttacker().send((Player) attacker, "victim", victim.getName());
        }

        if(!this.combatCache.getCombat(victim.getUniqueId()).isPresent()) {
            this.combatConfig.getCombatStartMessageVictim().send((Player) victim, "attacker", attacker.getName());
        }
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
        if(entity == null) {
            return;
        }

        final UUID entityId = entity.getUniqueId();

        if (isPlayer) {
            this.combatCache.removeCombat(entityId);
        }

        if (!this.combatConfig.isRemoveCombatOnOpponentDeath() || !(entity instanceof Player)) {
            return;
        }

        this.combatCache.findCombatPlayerByOpponent(entityId).ifPresent(playerId -> {
            this.combatCache.removeCombat(playerId);

            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);

            if (offlinePlayer.isOnline()) {
                final Player player = (Player) offlinePlayer;
                this.combatConfig.getRemoveCombatMessage().forEach(message -> message.send(player, "opponent", entity.getName()));
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if(!this.combatCache.getCombat(player.getUniqueId()).isPresent()) {
            return;
        }

        if (!this.hasBypassPermission(player)) {
            player.setHealth(0);
        }

        this.combatCache.removeCombat(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String command = event.getMessage().split(" ")[0];

        if (!this.combatConfig.isCommandsBlockedDuringCombat()
                || this.hasBypassPermission(player)
                || this.combatConfig.getCombatCommandWhitelist().contains(command)
                || !this.combatCache.getCombat(player.getUniqueId()).isPresent()) {
            return;
        }

        event.setCancelled(true);
        this.combatConfig.getCombatCommandBlockedMessage().forEach(message -> message.send(player, "command", command));
    }

    private boolean hasBypassPermission(Player player) {
        return player.hasPermission(this.combatConfig.getCombatBypassPermission());
    }
}
