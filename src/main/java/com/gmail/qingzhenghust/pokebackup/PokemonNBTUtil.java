package com.gmail.qingzhenghust.pokebackup;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;

public class PokemonNBTUtil {
    public static CompoundNBT getNbt(PokemonStorage storage){
        CompoundNBT nbt = new CompoundNBT();
        storage.writeToNBT(nbt);
        return nbt;
    }
    public static void writeNbtToFile(CompoundNBT nbt, String path) throws IOException{
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(path));
        CompressedStreamTools.write(nbt, dataOutputStream);
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    public static CompoundNBT readNbtFromFile(String path) throws IOException{
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(path));
        CompoundNBT nbt = CompressedStreamTools.read(dataInputStream);
        dataInputStream.close();
        return nbt;
    }
}
