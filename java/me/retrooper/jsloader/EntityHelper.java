package me.retrooper.jsloader;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class EntityHelper {
    public static Entity fromId(String world, int entityId) {
        for (Entity entity : Bukkit.getWorld(world).getEntities()) {
            if (entity.getEntityId() == entityId)
                return entity;
        }
        return null;
    }
}
