package com.petrolpark.destroy.content.redstone.programmer;

import java.util.function.Supplier;

import com.petrolpark.network.packet.S2CPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class RedstoneProgramSyncReplyS2CPacket extends S2CPacket {

    public RedstoneProgramSyncReplyS2CPacket() {

    };  
    
    public RedstoneProgramSyncReplyS2CPacket(FriendlyByteBuf buffer) {

    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {

    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        supplier.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player.containerMenu instanceof RedstoneProgrammerMenu menu) {
                menu.refreshSlots();
            };
        });
        return true;
    };


};
