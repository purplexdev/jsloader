package me.retrooper.jsloader;

import java.lang.reflect.Field;
import me.retrooper.jsloader.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerHelper {
    public static Player toPlayer(String uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public static Player fromId(int entityId) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.getEntityId() == entityId)
                return target;
        }
        return null;
    }

    public static Material materialUnderPlayer(int entityId) {
        Player p = fromId(entityId);
        return p.getLocation().add(0.0D, -0.3D, 0.0D).getBlock().getType();
    }

    public static int getPing(int entityId) {
        Player p = fromId(entityId);
        int ping = 0;
        try {
            Object entityPlayer = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
            ping = ((Integer)entityPlayer.getClass().getField("ping").get(entityPlayer)).intValue();
        } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|SecurityException|NoSuchFieldException e) {
            e.printStackTrace();
        }
        return ping;
    }

    public static void broadcast(String message) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
    }

    public static void sendMessage(int entityId, String message) {
        fromId(entityId).sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static Material materialUnderLocation(Location loc) {
        return loc.add(0.0D, -0.3D, 0.0D).getBlock().getType();
    }

    public static boolean onGround(Location loc) {
        double expand = 0.3D;
        for (double x = -expand; x <= expand; x += expand) {
            double z;
            for (z = -expand; z <= expand; z += expand) {
                if (loc.clone().add(x, -0.5001D, z).getBlock().getType() != Material.AIR)
                    return true;
            }
        }
        return false;
    }

    public static boolean onStairs(Location loc) {
        double expand = 0.3D;
        double x;
        for (x = -expand; x <= expand; x += expand) {
            double z;
            for (z = -expand; z <= expand; z += expand) {
                if (loc.clone().add(x, -0.5001D, z).getBlock().getType().name().contains("STAIRS"))
                    return true;
            }
        }
        expand = 1.0E-7D;
        for (x = -expand; x <= expand; x += expand) {
            double z;
            for (z = -expand; z <= expand; z += expand) {
                if (loc.clone().add(x, -0.5001D, z).getBlock().getType().name().contains("STAIRS"))
                    return true;
            }
        }
        return false;
    }

    public static double getMotion(int entityId, String type) {
        Player p = fromId(entityId);
        Reflection.MethodInvoker getPlayerHandle = Reflection.getMethod("{obc}.entity.CraftPlayer", "getHandle", new Class[0]);
        Object entityplayer = getPlayerHandle.invoke(p, new Object[0]);
        Field motField = null;
        try {
            motField = entityplayer.getClass().getField("mot" + type.trim().toUpperCase());
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        double mot = 0.0D;
        try {
            mot = motField.getDouble(entityplayer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return mot;
    }
}
