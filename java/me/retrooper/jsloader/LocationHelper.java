package me.retrooper.jsloader;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationHelper {
    public static Location toLoc(String worldName, double x, double y, double z) {
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }
}
