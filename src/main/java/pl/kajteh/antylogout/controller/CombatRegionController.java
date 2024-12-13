package pl.kajteh.antylogout.controller;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import pl.kajteh.antylogout.CombatCache;
import pl.kajteh.antylogout.config.CombatConfig;

import java.util.Set;

public class CombatRegionController implements Listener {

    private final CombatCache combatCache;
    private final CombatConfig combatConfig;
    private final WorldGuard worldGuard;

    public CombatRegionController(CombatCache combatCache, CombatConfig combatConfig) {
        this.combatConfig = combatConfig;
        this.combatCache = combatCache;
        this.worldGuard = WorldGuard.getInstance();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location to = event.getTo();
        final Location from = event.getFrom();

        if (to == null || from.distance(to) == 0 || !this.combatCache.getCombat(player.getUniqueId()).isPresent()) {
            return;
        }

        if (player.hasPermission(this.combatConfig.getCombatBypassPermission())
                || !this.combatCache.getCombat(player.getUniqueId()).isPresent()) {
            return;
        }

        final RegionContainer regionContainer = this.worldGuard.getPlatform().getRegionContainer();
        final RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(player.getWorld()));

        if (regionManager == null) {
            return;
        }

        final BlockVector3 playerVector = BukkitAdapter.adapt(player.getLocation()).toVector().toBlockPoint();
        final Set<ProtectedRegion> regions = regionManager.getApplicableRegions(playerVector).getRegions();

        regions.stream()
                .filter(region -> this.combatConfig.getCombatBlockedRegions().contains(region.getId()))
                .findFirst()
                .ifPresent(region -> {
                    final Location regionCenter = this.getRegionCenter(region, player);
                    final Location playerOffset = player.getLocation().subtract(regionCenter);

                    final Vector knockbackDirection = new Vector(playerOffset.getX(), 0, playerOffset.getZ()).normalize();

                    final double knockbackMultiplier = this.combatConfig.getCombatBlockedRegionKnockbackMultiplier();
                    final Vector knockbackStrength = new Vector(knockbackMultiplier, 0.5, knockbackMultiplier);

                    player.setVelocity(knockbackDirection.multiply(knockbackStrength));

                    this.combatConfig.getCombatBlockedRegionEnterMessage()
                            .forEach(message -> message.send(player, "region", region.getId()));
                });
    }

    private Location getRegionCenter(ProtectedRegion region, Player player) {
        final BlockVector3 regionMinCorner = region.getMinimumPoint();
        final BlockVector3 regionMaxCorner = region.getMaximumPoint();

        final double regionCenterX = (regionMinCorner.getX() + regionMaxCorner.getX()) / 2.0;
        final double regionCenterY = (regionMinCorner.getY() + regionMaxCorner.getY()) / 2.0;
        final double regionCenterZ = (regionMinCorner.getZ() + regionMaxCorner.getZ()) / 2.0;

        return new Location(player.getWorld(), regionCenterX, regionCenterY, regionCenterZ);
    }
}
