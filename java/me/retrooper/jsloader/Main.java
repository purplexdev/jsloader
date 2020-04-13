package me.retrooper.jsloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import me.retrooper.jsloader.cmd.JSLoaderCommand;
import me.retrooper.jsloader.listener.JSEvents;
import me.retrooper.jsloader.plugin.JSPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static JSPlugin[] plugins;

    private static Main instance;

    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents((Listener) new JSEvents(), (Plugin) this);
        saveDefaultConfig();
        loadFiles();
        for (int i = 0; i < plugins.length; i++) {
            plugins[i].onEnable();
        }
        Bukkit.getPluginCommand("jsloader").setExecutor((CommandExecutor) new JSLoaderCommand());
        Bukkit.getPluginCommand("jsl").setExecutor((CommandExecutor) new JSLoaderCommand());
        Bukkit.getPluginCommand("javascriptloader").setExecutor((CommandExecutor) new JSLoaderCommand());
    }

    public void onDisable() {
        for (int i = 0; i < plugins.length; i++) {
            plugins[i].onDisable();
        }
    }

    public void loadFiles() {
        File scriptsFolder = new File(getDataFolder() + "//Scripts");
        if (!scriptsFolder.exists()) {
            scriptsFolder.mkdir();
        }
        File[] scripts = scriptsFolder.listFiles();
        int len = scripts.length;
        if (len == 0)
            return;
        plugins = new JSPlugin[len];
        for (int i = 0; i < len; i++) {
            String content = readFileContent(scripts[i]);
            plugins[i] = new JSPlugin(content);
        }
    }

    public void reloadPlugin() {
        getPluginLoader().disablePlugin((Plugin) this);
        getPluginLoader().enablePlugin((Plugin) this);
    }

    private String readFileContent(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
                contentBuilder.append(sCurrentLine).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static Main getInstance() {
        return instance;
    }
}
