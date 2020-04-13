package me.retrooper.jsloader.data;

import me.retrooper.jsloader.plugin.JSPlugin;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class JSData {
    private JSPlugin plugin;

    public JSData(JSPlugin plugin) {
        this.plugin = plugin;
    }

    public void log(String message) {
        this.plugin.callMainFunc("log", message );
    }

    public boolean onMove(Player p, Location from, Location to) {
        String worldName = to.getWorld().getName();
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreMove",worldName, Double.valueOf(from.getX()),
                Double.valueOf(from.getY()), Double.valueOf(from.getZ()), Double.valueOf(to.getX()), Double.valueOf(to.getY()), Double.valueOf(to.getZ()), Integer.valueOf(p.getEntityId()));
        return !(boolean)(obj);
    }

    public boolean onJoin(Player p) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreJoin",  Integer.valueOf(p.getEntityId()) );
        return !(boolean)(obj);
    }

    public boolean onQuit(Player p) {
        this.plugin.callCustomFunc(this.plugin.getEvents(), "onQuit", Integer.valueOf(p.getEntityId()));
        return true;
    }

    public boolean onAttack(Entity damager, Entity target) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreAttack", new Object[] { damager.getWorld().getName(),
                Integer.valueOf(damager.getEntityId()), Integer.valueOf(target.getEntityId()),
                Double.valueOf(damager.getLocation().getX()), Double.valueOf(damager.getLocation().getY()), Double.valueOf(damager.getLocation().getZ()),
                Double.valueOf(target.getLocation().getX()), Double.valueOf(target.getLocation().getY()), Double.valueOf(target.getLocation().getZ()) });
        return !(boolean)(obj);
    }

    public boolean onVelocity(Player p) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreVelocity", p.getEntityId() );
        return !((Boolean)obj).booleanValue();
    }

    public boolean onChat(Player p, String message) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreChat",  p.getEntityId(), message );
        return !(boolean)(obj);
    }

    public boolean onBlockBreak(Player p, String materialName) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreBlockBreak", p.getEntityId(), materialName );
        return !(boolean)(obj);
    }

    public boolean onBlockPlace(Player p, String materialName) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreBlockPlace", p.getEntityId(), materialName );
        return !(boolean)(obj);
    }

    public boolean onCommand(Player p, String name, String[] args) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getCommands(), "onPreCommand",  p.getEntityId(), name, args);
        return !(boolean)(obj);
    }

    public boolean onInteractEntity(Player p, Entity entity) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreInteractAtEntity",p.getWorld().getName(),
                Integer.valueOf(p.getEntityId()), Integer.valueOf(entity.getEntityId()) );
        return !(boolean)(obj);
    }

    public boolean onEat(Player p, ItemStack stack) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreEat", p.getEntityId(), stack.getType().name());
        return !(boolean)(obj);
    }

    public boolean onToggleSneak(Player p, boolean isSneaking) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreToggleSneak", p.getEntityId(), isSneaking);
        return !(boolean)(obj);
    }

    public boolean onItemPickup(Player p, Item item) {
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreItemPickup", p.getEntityId(), item.getType());
        return !(boolean)(obj);
    }

    public boolean onTeleport(Player p, Location from, Location to, PlayerTeleportEvent.TeleportCause cause) {
        double fX = from.getX();
        double fY = from.getY();
        double fZ = from.getZ();
        double tX = to.getX();
        double tY = to.getY();
        double tZ = to.getZ();
        Object obj = this.plugin.callCustomFunc(this.plugin.getEvents(), "onPreTeleport", p.getEntityId(),from.getWorld().getName(), to.getWorld().getName(), fX, fY, fZ, tX, tY, tZ, cause.name());
        return !(boolean) (obj);
    }
    public void sendMessage(Player p, String message) {
        p.sendMessage(message);
    }

}
