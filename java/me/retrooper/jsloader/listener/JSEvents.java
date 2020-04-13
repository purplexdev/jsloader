package me.retrooper.jsloader.listener;

import java.util.ArrayList;

import me.retrooper.jsloader.Main;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class JSEvents implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++) {
            if ((Main.plugins[i]).data.onJoin(e.getPlayer()))
                e.getPlayer().kickPlayer("Failed to join the server.");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++)
            (Main.plugins[i]).data.onQuit(e.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++) {
            if ((Main.plugins[i]).data.onMove(e.getPlayer(), e.getFrom(), e.getTo()))
                e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++)
            e.setCancelled((Main.plugins[i]).data.onAttack(e.getDamager(), e.getEntity()));
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++)
            e.setCancelled((Main.plugins[i]).data.onVelocity(e.getPlayer()));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++)
            e.setCancelled((Main.plugins[i]).data.onChat(e.getPlayer(), e.getMessage()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++)
            e.setCancelled((Main.plugins[i]).data.onBlockBreak(e.getPlayer(), e.getBlock().getType().name()));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++)
            e.setCancelled((Main.plugins[i]).data.onBlockPlace(e.getPlayer(), e.getBlock().getType().name()));
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++)
            e.setCancelled((Main.plugins[i]).data.onInteractEntity(e.getPlayer(), e.getRightClicked()));
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++)
            e.setCancelled(Main.plugins[i].data.onEat(e.getPlayer(), e.getItem()));
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++) {
            e.setCancelled(Main.plugins[i].data.onToggleSneak(e.getPlayer(), e.isSneaking()));
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++) {
            e.setCancelled(
                    Main.plugins[i].data.onItemPickup(e.getPlayer(), e.getItem()));
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        int len = Main.plugins.length;
        for (int i = 0; i < len; i++) {
            e.setCancelled(
                    Main.plugins[i].data.onTeleport(e.getPlayer(), e.getFrom(), e.getTo(), e.getCause()));
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String name = "";
        ArrayList<String> arguments = new ArrayList<>();
        for (int i = 0; i < (e.getMessage().split(" ")).length; i++) {
            if (i != 0) {
                arguments.add(e.getMessage().split(" ")[i]);
            } else {
                name = e.getMessage().split(" ")[i].substring(1);
            }
        }
        String[] args = arguments.<String>toArray(new String[arguments.size()]);
        int len = Main.plugins.length;
        for (int j = 0; j < len; j++)
            e.setCancelled(!(Main.plugins[j]).data.onCommand(e.getPlayer(), name, args));
    }
}
