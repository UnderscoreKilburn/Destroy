package com.petrolpark.destroy.core.pollution;

import java.util.function.Supplier;

import com.petrolpark.network.packet.S2CPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class LevelPollutionS2CPacket extends S2CPacket {
    
    private final Pollution levelPollution;

    public LevelPollutionS2CPacket(Pollution levelPollution) {
        this.levelPollution = levelPollution;
    };

    public LevelPollutionS2CPacket(FriendlyByteBuf buffer) {
        this.levelPollution = new Pollution.Level();
        levelPollution.loadNBTData(buffer.readNbt());
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        levelPollution.saveNBTData(tag);
        buffer.writeNbt(tag);
    };
    
    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevelPollutionData.setLevelPollution(levelPollution); // Update the Level Pollution information
        });
        return true;
    };
};
