package com.gmail.qingzhenghust.pokebackup;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Player;

import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.listener.PixelmonPlayerTracker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class BackupAPI {   
    public static String getDir(String backupId){
        return Main.plugin.pluginPath+"/data/"+backupId+"/";
    }
    public static void backUpForAllPlayer(String backupId, boolean log){
        for(Player player : Bukkit.getOnlinePlayers()){
            String playerName = player.getName();
            backUpForPlayer(playerName, getDir(backupId));
        }
        List<String> logs = Main.plugin.getConfig().getStringList("Logs");
        if(log){
            logs.add(backupId);
        }
        if(logs.size() > Main.plugin.getConfig().getInt("Settings.maxBackupTimes")){
            logs.remove(0);
            File file = new File(getDir(backupId));
            if(file.exists()){
                File delFile[]=file.listFiles();
                int i = file.listFiles().length;
                for(int j=0; j<i; ++j){
                    delFile[j].delete();
                }
                file.delete();
            }
            
        }
        Main.plugin.getConfig().set("Logs", logs);
        Main.plugin.saveConfig();
    }
    public static void backUpForPlayer(String player, String dirPath){
        PlayerPartyStorage playerPartyStorage = StorageUtil.getStorage(player);
        PCStorage pcStorage = StorageUtil.getPCStorage(player);
        CompoundNBT ppsNBT = PokemonNBTUtil.getNbt(playerPartyStorage);
        CompoundNBT pcNBT = PokemonNBTUtil.getNbt(pcStorage);
        File file = new File(dirPath);
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            PokemonNBTUtil.writeNbtToFile(ppsNBT, dirPath+player+"_pps.bu");
            PokemonNBTUtil.writeNbtToFile(pcNBT, dirPath+player+"_pc.bu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean recoverForPlayer(String player, String dirPath){
        File file = new File(dirPath+"/"+player+"_pps.bu");
        if(!file.exists()){
            return false;
        }
        PlayerPartyStorage playerPartyStorage = StorageUtil.getStorage(player);
        PCStorage pcStorage = StorageUtil.getPCStorage(player);
        try {
            CompoundNBT nbt = PokemonNBTUtil.readNbtFromFile(dirPath+"/"+player+"_pps.bu");
            playerPartyStorage.readFromNBT(nbt);
            playerPartyStorage.setHasChanged(true);
            playerPartyStorage.shouldSendUpdates = true;
            PlayerEvent.PlayerLoggedInEvent playerLoginEvent = new PlayerEvent.PlayerLoggedInEvent((PlayerEntity)StorageUtil.getServerPlayerEntity(player).getEntity());
            PixelmonPlayerTracker.onPlayerLogin(playerLoginEvent);
            nbt = PokemonNBTUtil.readNbtFromFile(dirPath+"/"+player+"_pc.bu");
            pcStorage.readFromNBT(nbt);
            pcStorage.setHasChanged(true);
            pcStorage.shouldSendUpdates = true;
            StorageProxy.initializePCForPlayer(StorageUtil.getServerPlayerEntity(player), pcStorage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
