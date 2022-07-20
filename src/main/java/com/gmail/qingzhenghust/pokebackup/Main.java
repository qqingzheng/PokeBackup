package com.gmail.qingzhenghust.pokebackup;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Main extends JavaPlugin implements Listener{
    public static Main plugin;
    public String pluginPath;
    public BukkitTask task;
    @Override
    public void onEnable(){
        plugin = this;
        pluginPath = this.getDataFolder().getPath();
        Bukkit.getPluginCommand("pokebackup").setExecutor(new CommandListener());
        init();
        List<String> logs = plugin.getConfig().getStringList("Logs");
        if(logs.size() > 0){
            List<String> freezelogs = plugin.getConfig().getStringList("FreezeLogs");
            freezelogs.add(logs.get(logs.size()-1));
            logs.remove(logs.size()-1);
            plugin.getConfig().set("FreezeLogs", freezelogs);
            plugin.getConfig().set("Logs", logs);
            plugin.saveConfig();
        }
        runTask();
    }
    @Override
    public void onDisable(){
        BackupAPI.backUpForAllPlayer(String.valueOf(System.currentTimeMillis()), true);
    }
    public void init(){
        saveDefaultConfig();
        File file = new File(this.pluginPath+"/data/");
        if(file.exists()){
            file.mkdirs();
        }
    }
    public void runTask(){
        task = new BukkitRunnable() { public void run(){
			BackupAPI.backUpForAllPlayer(String.valueOf(System.currentTimeMillis()), true);
        }}.runTaskTimerAsynchronously(this, this.getConfig().getInt("Settings.backUpInterval")*20, this.getConfig().getInt("Settings.backUpInterval")*20);
    }
}
