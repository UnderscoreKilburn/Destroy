package com.petrolpark.destroy.core.extendedinventory;

import java.util.function.Supplier;

import com.petrolpark.network.packet.C2SPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class RequestInventoryFullStateC2SPacket extends C2SPacket {

    @Override
    public void toBytes(FriendlyByteBuf buffer) {};

    @Override
    public boolean handle(Supplier<Context> supplier) {
        supplier.get().enqueueWork(() -> {
            supplier.get().getSender().inventoryMenu.broadcastFullState();
            supplier.get().getSender().inventoryMenu.sendAllDataToRemote();
        });
        return true;
    };
    
};
