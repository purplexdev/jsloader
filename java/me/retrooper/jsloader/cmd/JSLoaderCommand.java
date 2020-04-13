package me.retrooper.jsloader.cmd;

import me.retrooper.jsloader.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class JSLoaderCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.DARK_GREEN + "JSLoader" + ChatColor.GREEN + " made by Retrooper");
            sender.sendMessage(ChatColor.DARK_GREEN + "My Website:" + ChatColor.GREEN + " https://retrooper.cf");
            sender.sendMessage(ChatColor.GOLD + "My SpigotMC:" + ChatColor.GREEN + " https://www.spigotmc.org/resources/authors/retrooper.631270/");
            sender.sendMessage(ChatColor.DARK_GREEN + "Donation via PayPal: " + ChatColor.GREEN + " https://paypal.me/Uchizi");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                Main.getInstance().reloadPlugin();
                Main.getInstance().reloadConfig();
                sender.sendMessage(ChatColor.DARK_PURPLE + "JSLoader reloaded. All scripts reloaded.");
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid arguments..");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Too many arguments..");
        }
        return true;
    }
}