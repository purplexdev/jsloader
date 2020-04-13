package me.retrooper.jsloader.utils;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BukkitUtils {
    public static int getPotionLevel(Player p, PotionEffectType type) {
        for (PotionEffect pEffect : p.getActivePotionEffects()) {
            if (pEffect.getType().equals(type))
                return pEffect.getAmplifier() + 1;
        }
        return 0;
    }

    public static Player[] getPlayers() {
        Player[] list = new Player[Bukkit.getOnlinePlayers().size()];
        int i = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            list[i] = p;
            i++;
        }
        return list;
    }

    public static ArrayList<String> getPlayerNames() {
        ArrayList<String> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers())
            list.add(p.getName());
        return list;
    }
}
