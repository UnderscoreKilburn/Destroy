package com.petrolpark.destroy.core.chemistry.vat.observation;

import java.util.function.Supplier;

import com.petrolpark.network.packet.C2SPacket;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class RedstoneQuantityMonitorThresholdChangeC2SPacket extends C2SPacket {

    public final boolean upper;
    public final float value;
    public final BlockPos pos;

    public RedstoneQuantityMonitorThresholdChangeC2SPacket(boolean upper, float value, BlockPos pos) {
        this.upper = upper;
        this.value = value;
        this.pos = pos;
    };

    public RedstoneQuantityMonitorThresholdChangeC2SPacket(FriendlyByteBuf buffer) {
        upper = buffer.readBoolean();
        value = buffer.readFloat();
        pos = buffer.readBlockPos();
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(upper);
        buffer.writeFloat(value);
        buffer.writeBlockPos(pos);
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            RedstoneQuantityMonitorBehaviour behaviour = BlockEntityBehaviour.get(sender.level(), pos, RedstoneQuantityMonitorBehaviour.TYPE);
            if (behaviour != null) {
                if (upper) behaviour.upperThreshold = value;
                else behaviour.lowerThreshold = value;
                behaviour.notifyUpdate();
            };
        });
        return true;
    };
    
};
