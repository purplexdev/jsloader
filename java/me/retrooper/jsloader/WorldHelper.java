package me.retrooper.jsloader;

import org.bukkit.World;

public class WorldHelper {
    public static World toWorld(int id) {
        return PlayerHelper.fromId(id).getWorld();
    }
}
