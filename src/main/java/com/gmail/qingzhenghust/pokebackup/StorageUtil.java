package com.gmail.qingzhenghust.pokebackup;

import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;

import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;

import net.minecraft.entity.player.ServerPlayerEntity;

public class StorageUtil {
    public static ServerPlayerEntity getServerPlayerEntity(String playerName) {
        return PixelmonCommandUtils.getEntityPlayer(playerName);
    }
    public static PlayerPartyStorage getStorage(String playerName){
        ServerPlayerEntity serverPlayerEntity = getServerPlayerEntity(playerName);
        return StorageProxy.getParty(serverPlayerEntity);

    }
    public static PCStorage getPCStorage(String playerName){
        ServerPlayerEntity serverPlayerEntity = getServerPlayerEntity(playerName);
        return StorageProxy.getPCForPlayer(serverPlayerEntity);

    }
}
