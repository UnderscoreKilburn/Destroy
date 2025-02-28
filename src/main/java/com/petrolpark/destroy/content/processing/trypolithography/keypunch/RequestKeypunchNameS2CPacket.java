package com.petrolpark.destroy.content.processing.trypolithography.keypunch;

import java.util.List;
import java.util.function.Supplier;

import com.petrolpark.network.packet.S2CPacket;
import com.petrolpark.destroy.DestroyMessages;
import com.petrolpark.destroy.util.NameLists;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class RequestKeypunchNameS2CPacket extends S2CPacket {

    public final BlockPos pos;

    public RequestKeypunchNameS2CPacket(BlockPos pos) {
        this.pos = pos;
    };

    public RequestKeypunchNameS2CPacket(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        supplier.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            List<String> names = NameLists.getNames(KeypunchBlock.NAME_LIST_ID);
            DestroyMessages.sendToServer(new NameKeypunchC2SPacket(pos, names.get(minecraft.level.getRandom().nextInt(names.size()))));
        });
        return true;
    };
    
};
