package com.petrolpark.destroy.core.pollution;

import java.util.function.Supplier;

import com.petrolpark.network.packet.S2CPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher.RenderChunk;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkEvent.Context;

public class SyncChunkPollutionS2CPacket extends S2CPacket {

    public final ChunkPos pos;
    private final CompoundTag chunkPollutionTag;

    public SyncChunkPollutionS2CPacket(FriendlyByteBuf buffer) {
        pos = buffer.readChunkPos();  
        chunkPollutionTag = buffer.readNbt();
    };

    public <P extends Pollution> SyncChunkPollutionS2CPacket(ChunkPos pos, P pollution) {
        this.pos = pos;
        this.chunkPollutionTag = new CompoundTag();
        pollution.saveNBTData(chunkPollutionTag);
    };

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeChunkPos(pos);
        buffer.writeNbt(chunkPollutionTag);
    };

    @Override
    public boolean handle(Supplier<Context> supplier) {
        supplier.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            LevelChunk chunk = mc.level.getChunkSource().getChunk(pos.x, pos.z, false);
            if (chunk != null) chunk.getCapability(Pollution.CAPABILITY).ifPresent(pollution -> {
                pollution.loadNBTData(chunkPollutionTag);
                if (((Pollution.Chunk)pollution).checkRerender()) {
                    for (RenderChunk renderChunk : mc.levelRenderer.viewArea.chunks) renderChunk.setDirty(true);
                    mc.level.clearTintCaches();
                };
            });
        });
        return true;
    };
    
};
