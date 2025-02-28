package com.petrolpark.destroy.content.processing.trypolithography;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.function.Supplier;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.network.packet.S2CPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent.Context;

public class CircuitPatternsS2CPacket extends S2CPacket {

    public final Map<ResourceLocation, Integer> patterns;

    public CircuitPatternsS2CPacket(Map<ResourceLocation, Integer> patterns) {
        this.patterns = patterns;
    };

    public static CircuitPatternsS2CPacket read(FriendlyByteBuf buffer) {
        int entries = buffer.readVarInt();
        Map<ResourceLocation, Integer> patterns = new HashMap<>(entries);
        for (int i = 0; i < entries; i++) {
            patterns.put(buffer.readResourceLocation(), (int)Short.MAX_VALUE + (int)buffer.readShort());
        };
        return new CircuitPatternsS2CPacket(patterns);
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeVarInt(patterns.size());
        for (Entry<ResourceLocation, Integer> entry : patterns.entrySet()) {
            buffer.writeResourceLocation(entry.getKey());
            buffer.writeShort((short)(entry.getValue() - Short.MAX_VALUE));
        };
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        supplier.get().enqueueWork(() -> {
            Destroy.CIRCUIT_PATTERN_HANDLER.setPatterns(patterns);
        });
        return true;
    };
    
};
