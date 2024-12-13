package pl.kajteh.antylogout.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class EntityUtil {

    public static String getEntityName(Entity entity) {
        if(entity instanceof Player) {
            return entity.getName();
        }

        if (entity instanceof LivingEntity) {
            final LivingEntity livingEntity = (LivingEntity) entity;
            return (livingEntity.getCustomName() != null ? livingEntity.getCustomName() : livingEntity.getType().name().toLowerCase()).trim();
        }

        return "Unknown";
    }
}
