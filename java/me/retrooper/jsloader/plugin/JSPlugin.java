package me.retrooper.jsloader.plugin;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import me.retrooper.jsloader.Main;
import me.retrooper.jsloader.data.JSData;
import me.retrooper.jsloader.exceptions.MissingRequiredFunctionException;
import org.bukkit.event.HandlerList;

import java.util.function.Function;

public class JSPlugin {
    private ScriptEngine engine;

    private CompiledScript compiledScript;

    private Object main;

    private Object events;

    private Object commands;

    public JSData data;

    private String code = "var main = new Object();main.log = function(name) { print(name) };main.playerCount = function() {return Bukkit.getOnlinePlayers().length;};main.getPotionAmplifier = function(player, type) { return BukkitUtils.getPotionLevel(player, type);};var events = new Object();var commands = new Object();";

    public JSPlugin(String code) {
        this.code += code;
        this.engine = (new ScriptEngineManager()).getEngineByName("nashorn");
        compile();
        this.data = new JSData(this);
    }

    public JSPlugin() {
        this.engine = (new ScriptEngineManager()).getEngineByName("nashorn");
        compile();
        this.data = new JSData(this);
    }

    private void compile() {
        String code = getCode();
        if (code.trim().isEmpty())
            return;
        try {
            this.compiledScript = ((Compilable) this.engine).compile(code);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public void onEnable() {
        eval();
        this.main = this.engine.get("main");
        try {
            this.events = this.engine.get("events");
            this.commands = this.engine.get("commands");
        } catch (IllegalArgumentException illegalArgumentException) {
        }
        callCustomFunc(this.main, "onEnable", new Object[0]);
    }

    public void onDisable() {
        unregisterEvents();
        callCustomFunc(this.main, "onDisable", new Object[0]);
    }

    public String getCode() {
        return this.code;
    }

    public Object getMain() {
        return this.main;
    }

    public Object getEvents() {
        return this.events;
    }

    public Object getCommands() {
        return this.commands;
    }

    public void callMainFunc(String name, Object... arguments) {
        Invocable inv = (Invocable) this.engine;
        try {
            try {
                inv.invokeMethod(this.main, name, arguments);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public Object callCustomFunc(Object obj, String name, Object... arguments) {
        Invocable inv = (Invocable) this.engine;
        try {
            return inv.invokeMethod(obj, name, arguments);
        } catch (ScriptException e) {
            if (obj == getCommands())
                return Boolean.valueOf(false);
            if (obj == getEvents())
                return Boolean.valueOf(true);
        } catch (NoSuchMethodException e) {
            if (obj == getEvents() || obj == getCommands())
                return Boolean.valueOf(true);
            if ("onEnable".equals(name))
                throw new MissingRequiredFunctionException("onEnable function is missing, it is required!");
            if ("onDisable".equals(name))
                throw new MissingRequiredFunctionException("onDisable function is missing, it is required!");
        }
        return Boolean.valueOf(true);
    }

    public void eval() {
        try {
            this.compiledScript.eval();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        engine.put("JavaClass", (Function<String, Class>) s -> {
            try {
                return Class.forName(s);
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException(cnfe);
            }
        });
        CompiledScript importedScript = null;
        String importCode = "";
        importCode += "var Bukkit = JavaClass('org.bukkit.Bukkit').static;";
        importCode += "var PotionEffectType = JavaClass('org.bukkit.potion.PotionEffectType').static;";
        importCode += "var LocationHelper = JavaClass('me.retrooper.jsloader.LocationHelper').static;";
        importCode += "var PlayerHelper = JavaClass('me.retrooper.jsloader.PlayerHelper').static;";
        importCode += "var EntityHelper = JavaClass('me.retrooper.jsloader.EntityHelper').static;";
        importCode += "var WorldHelper = JavaClass('me.retrooper.jsloader.WorldHelper').static;";
        importCode += "var Material = JavaClass('org.bukkit.Material').static;";
        importCode += "var BukkitUtils = JavaClass('me.retrooper.jsloader.utils.BukkitUtils').static;";
        importCode += "var ArrayList = JavaClass('java.util.ArrayList');";
        importCode += "var GameMode = JavaClass('org.bukkit.GameMode').static;";
        importCode += "var Entity = JavaClass('org.bukkit.entity.Entity');";
        importCode += "var Player = JavaClass('org.bukkit.entity.Player');";
        importCode += "var Location = JavaClass('org.bukkit.Location');";
        importCode += "var UUID = JavaClass('java.util.UUID');";
        importCode += "var ItemStack = JavaClass('org.bukkit.inventory.ItemStack');";
        importCode += "var TeleportCause = JavaClass('me.retrooper.jsloader.customenums.TeleportCause');";
        try {
            importedScript = ((Compilable) this.engine).compile(importCode);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        try {
            importedScript.eval();
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
        try {
            registerEvents();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        try {
            registerCommands();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public void registerEvents() throws ScriptException, NullPointerException {
        this.code += "main.broadcast = function(message) {PlayerHelper.broadcast(message);};";
        this.code += "main.message = function(player, message) {PlayerHelper.sendMessage(player.getEntityId(), message);};";
        this.code += "main.getPing = function(player) {return PlayerHelper.getPing(player.getEntityId());};";
        this.code += "main.getMotion = function(player, type) { return PlayerHelper.getMotion(player.getEntityId(), type);};";
        this.code += "main.blockUnderPlayer = function(player) {return PlayerHelper.materialUnderPlayer(player.getEntityId());};";
        this.code += "main.blockUnderLocation = function(location) {return PlayerHelper.materialUnderLocation(location);};";
        this.code += "main.onGround = function(location) {return PlayerHelper.onGround(location);};";
        this.code += "main.onStairs = function(location) {return PlayerHelper.onStairs(location);};";
        this.engine.eval("main.broadcast = function(message){PlayerHelper.broadcast(message);};");
        this.engine.eval("main.message = function(player, message) {PlayerHelper.sendMessage(player.getEntityId(), message);};");
        this.engine.eval("main.getPing = function(player) {return PlayerHelper.getPing(player.getEntityId());};");
        this.engine.eval("main.getMotion = function(player, type) { return PlayerHelper.getMotion(player.getEntityId(), type);};");
        this.engine.eval("main.blockUnderPlayer = function(player) {return PlayerHelper.materialUnderPlayer(player.getEntityId());};");
        this.engine.eval("main.blockUnderLocation = function(location) {return PlayerHelper.materialUnderLocation(location);};");
        this.engine.eval("main.onGround = function(location) {return PlayerHelper.onGround(location);};");
        this.engine.eval("main.onStairs = function(location) {return PlayerHelper.onStairs(location);};");
        String eventsCode = "";
        eventsCode += "events.onPreJoin = function(id){player = PlayerHelper.fromId(id); return events.onJoin(player);};";
        eventsCode += "events.onPreMove = function(worldName, fromX, fromY, fromZ, toX, toY, toZ, id) {player=PlayerHelper.fromId(id); from = LocationHelper.toLoc(worldName, fromX, fromY, fromZ); to = LocationHelper.toLoc(worldName, toX, toY, toZ); return events.onMove(player, from, to);};";
        eventsCode += "events.onPreQuit = function(id) {player=PlayerHelper.fromId(id); return events.onQuit(player)};";
        eventsCode += "events.onPreAttack = function(worldName, attackerId, targetId, aX, aY, aZ, tX, tY, tZ) { attacker = EntityHelper.fromId(worldName, attackerId); target = EntityHelper.fromId(worldName, targetId); return events.onAttack(attacker, target, LocationHelper.toLoc(worldName, aX, aY, aZ), LocationHelper.toLoc(worldName, tX, tY, tZ));};";
        eventsCode += "events.onPreVelocity = function(id){player = PlayerHelper.fromId(id);return events.onVelocity(player);};";
        eventsCode += "events.onPreChat = function(id, message){player = PlayerHelper.fromId(id); return events.onChat(player, message);};";
        eventsCode += "events.onPreBlockBreak = function(id, materialName){ player = PlayerHelper.fromId(id); var material = Material.valueOf(materialName); return events.onBlockBreak(player, material);};";
        eventsCode += "events.onPreBlockPlace = function(id, materialName){ player = PlayerHelper.fromId(id); var material = Material.valueOf(materialName); return events.onBlockPlace(player, material);};";
        eventsCode += "events.onPreInteractAtEntity = function(worldName, entityId, entityIdB){player = PlayerHelper.fromId(entityId); entity = EntityHelper.fromId(worldName, entityIdB); return events.onInteractAtEntity(player, entity);};";
        eventsCode += "events.onPreEat = function(id, materialname) {player = PlayerHelper.fromId(id); material = Material.valueOf(materialname); return events.onEat(player, material);};";
        eventsCode += "events.onPreToggleSneak = function(id, isSneaking) {player = PlayerHelper.fromId(id); return events.onToggleSneak(player, isSneaking);};";
        eventsCode += "events.onPreItemPickup = function(id, materialName) {player = PlayerHelper.fromId(id); material = Material.valueOf(materialName); return events.onItemPickup(player, material);};";
        eventsCode += "events.onPreTeleport = function(id, fwrld, twrld, fX, fY, fZ, tX, tY, tZ, causeName) {player = PlayerHelper.fromId(id); cause = TeleportCause.valueOf(causeName); from = LocationHelper.toLoc(fwrld, fX, fY, fZ); to = LocationHelper.toLoc(twrld, tX, tY, tZ); return events.onTeleport(player, from, to, cause);};";
        //COMPILE THEN EXECUTE

        ((Compilable)this.engine).compile(eventsCode).eval();

        //this.engine.eval("events.onPreJoin = function(id){player = PlayerHelper.fromId(id); return events.onJoin(player);};");
        //this.engine.eval("events.onPreMove = function(worldName, fromX, fromY, fromZ, toX, toY, toZ, id) {player=PlayerHelper.fromId(id); from = LocationHelper.toLoc(worldName, fromX, fromY, fromZ); to = LocationHelper.toLoc(worldName, toX, toY, toZ); return events.onMove(player, from, to);};");
        //this.engine.eval("events.onPreQuit = function(id) {player=PlayerHelper.fromId(id); return events.onQuit(player)};");
        //this.engine.eval("events.onPreAttack = function(worldName, attackerId, targetId, aX, aY, aZ, tX, tY, tZ) { attacker = EntityHelper.fromId(worldName, attackerId); target = EntityHelper.fromId(worldName, targetId); return events.onAttack(attacker, target, LocationHelper.toLoc(worldName, aX, aY, aZ), LocationHelper.toLoc(worldName, tX, tY, tZ));};");
        //this.engine.eval("events.onPreVelocity = function(id){player = PlayerHelper.fromId(id);return events.onVelocity(player);};");
        //this.engine.eval("events.onPreChat = function(id, message){player = PlayerHelper.fromId(id); return events.onChat(player, message);};");
        //this.engine.eval("events.onPreBlockBreak = function(id, materialName){ player = PlayerHelper.fromId(id); var material = Material.valueOf(materialName); return events.onBlockBreak(player, material);};");
        //this.engine.eval("events.onPreBlockPlace = function(id, materialName){ player = PlayerHelper.fromId(id); var material = Material.valueOf(materialName); return events.onBlockPlace(player, material);};");
        //this.engine.eval("events.onPreInteractAtEntity = function(worldName, entityId, entityIdB){player = PlayerHelper.fromId(entityId); entity = EntityHelper.fromId(worldName, entityIdB); return events.onInteractAtEntity(player, entity);};");
        //this.engine.eval("events.onPreEat = function(id, materialname) {player = PlayerHelper.fromId(id); material = Material.valueOf(materialname); return events.onEat(player, material);};");
        //this.engine.eval("events.onPreToggleSneak = function(id, isSneaking) {player = PlayerHelper.fromId(id); return onToggleSneak(player, isSneaking);};");
    }

    public void registerCommands() throws ScriptException {
        this.engine.eval("commands.onPreCommand = function(id, name, args){ player = PlayerHelper.fromId(id); return commands.onCommand(player, name, args);};");
    }

    public void unregisterEvents() {
        HandlerList.unregisterAll();
    }
}
