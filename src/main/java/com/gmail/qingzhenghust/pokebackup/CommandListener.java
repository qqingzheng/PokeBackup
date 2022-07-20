package com.gmail.qingzhenghust.pokebackup;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandListener implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(sender.isOp()){
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("help")){
                    sender.sendMessage("--- PokeBackup ---");
                    sender.sendMessage(" /pokebackup reload  重载配置");
                    sender.sendMessage(" /pokebackup unfreeze <ID> 解冻（删除）某条冻结的备份");
                    sender.sendMessage(" /pokebackup backup 为全体备份");
                    sender.sendMessage(" /pokebackup log 查看备份记录");
                    sender.sendMessage(" /pokebackup recover <log/freeze> <id> <player> 为某个玩家恢复数据");
                    return true;
                }
                if(args[0].equalsIgnoreCase("reload")){
                    Main.plugin.reloadConfig();
                    if(Main.plugin.task != null){
                        Main.plugin.task.cancel();
                    }
                    
                    Main.plugin.runTask();
                    sender.sendMessage("重载成功");
                    return true;
                }
                if(args[0].equalsIgnoreCase("backup")){
                    BackupAPI.backUpForAllPlayer(String.valueOf(System.currentTimeMillis()), true);
                    sender.sendMessage("备份成功");
                    return true;
                }
                if(args[0].equalsIgnoreCase("log")){
                    sender.sendMessage("--- PokeBackup ---");
                    List<String> list = Main.plugin.getConfig().getStringList("Logs");
                    sender.sendMessage("普通备份:");
                    for(int i = list.size() - 1; i >= 0; i--){
                        String time = TimeUtil.stampToDate(Long.valueOf(list.get(i)));
                        sender.sendMessage("§c§l["+i+"] §f" + time);
                    }
                    list = Main.plugin.getConfig().getStringList("FreezeLogs");
                    sender.sendMessage("冻结备份:");
                    for(int i = list.size() - 1; i >= 0; i--){
                        String time = TimeUtil.stampToDate(Long.valueOf(list.get(i)));
                        sender.sendMessage("§c§l["+i+"] §f" + time);
                    }
                    return true;
                }
            }
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("unfreeze")){
                    int id = Integer.valueOf(args[1]);
                    List<String> list = Main.plugin.getConfig().getStringList("FreezeLogs");
                    list.remove(id);
                    Main.plugin.getConfig().set("FreezeLogs", list);
                    Main.plugin.saveConfig();
                    sender.sendMessage("成功解冻");
                    return true;
                }
            }
            if(args.length == 4){
                if(args[0].equalsIgnoreCase("recover")){
                    String id = args[2];
                    String logtype = args[1];
                    String playerName = args[3];
                    if(logtype.equalsIgnoreCase("log")){
                        logtype = "Logs";
                    }else if(logtype.equalsIgnoreCase("freeze")){
                        logtype = "FreezeLogs";
                    }else{
                        sender.sendMessage("没有这个备份类型");
                        return true;
                    }
                    id = Main.plugin.getConfig().getStringList(logtype).get(Integer.valueOf(id));
                    if(BackupAPI.recoverForPlayer(playerName, BackupAPI.getDir(id))){
                        sender.sendMessage("已为玩家恢复数据");
                    }else{
                        sender.sendMessage("没有这名玩家的备份");
                    }
                    return true;
                }
            }
        }
        
        return false;
    }
    
}
