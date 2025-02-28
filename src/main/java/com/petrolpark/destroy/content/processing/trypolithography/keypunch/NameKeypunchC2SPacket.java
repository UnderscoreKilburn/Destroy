package com.petrolpark.destroy.content.processing.trypolithography.keypunch;

import java.util.function.Supplier;

import com.petrolpark.network.packet.C2SPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent.Context;

public class NameKeypunchC2SPacket extends C2SPacket {

    public final BlockPos pos;
    public final String name;

    public NameKeypunchC2SPacket(BlockPos pos, String name) {
        this.pos = pos;
        this.name = name;
    };

    public NameKeypunchC2SPacket(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        name = buffer.readUtf();
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeUtf(name);
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender  = context.getSender();
            if (sender == null) return;
            Level level = sender.level();
            if (level == null) return;
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof KeypunchBlockEntity keypunch) {
                keypunch.name = name;
                keypunch.notifyUpdate();  
            };
        });
        return true;
    };
    
};
