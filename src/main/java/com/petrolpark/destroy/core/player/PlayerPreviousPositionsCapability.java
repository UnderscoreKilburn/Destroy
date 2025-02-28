package com.petrolpark.destroy.core.player;

import java.util.List;
import java.util.Queue;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.EvictingQueue;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.config.DestroyAllConfigs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Destroy.MOD_ID)
public class PlayerPreviousPositionsCapability {

    public static final Capability<PlayerPreviousPositionsCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerPreviousPositionsCapability>() {});

    private static int QUEUE_SIZE = 20; // Default is 20
    private static final int TICKS_PER_SECOND = 20;

    private Queue<BlockPos> previousPositions = EvictingQueue.create(QUEUE_SIZE);
    private int tickCounter = 0;

    public BlockPos getOldestPosition() {
        return previousPositions.peek();
    };

    public void recordPosition(BlockPos pos) {
        previousPositions.add(pos);
    };

    public void clearPositions() {
        previousPositions.clear();
    };

    public void incrementTickCounter() {
        tickCounter++;
        if (tickCounter >= TICKS_PER_SECOND) {
            tickCounter = 0;
        };
    };

    public boolean hasBeenSecond() {
        return tickCounter == 0;
    };

    public void saveNBTData(CompoundTag tag) {
        ListTag positionsTag = new ListTag();
        for (BlockPos pos : previousPositions) {
            positionsTag.add(new IntArrayTag(List.of(pos.getX(), pos.getY(), pos.getZ())));
        };
        tag.put("PreviousPositions", positionsTag);
    };

    public void loadNBTData(CompoundTag tag) {
        previousPositions = EvictingQueue.create(QUEUE_SIZE);
        ListTag positionsTag = tag.getList("PreviousPositions", Tag.TAG_INT_ARRAY);
        for (int i = 0; i < positionsTag.size(); i++) {
            int[] posTag = positionsTag.getIntArray(i);
            previousPositions.add(new BlockPos(posTag[0], posTag[1], posTag[2]));
        };
    };

    public static void updateQueueSize() {
        QUEUE_SIZE = DestroyAllConfigs.SERVER.substances.chorusWineTeleportTime.get();
    };

    public static int getQueueSize() {
        return QUEUE_SIZE;
    };

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    
        private PlayerPreviousPositionsCapability previousPositions = null;
        private final LazyOptional<PlayerPreviousPositionsCapability> optional = LazyOptional.of(this::createPlayerPreviousPositions);
    
        private PlayerPreviousPositionsCapability createPlayerPreviousPositions() {
            PlayerPreviousPositionsCapability.updateQueueSize();
            if (previousPositions == null) {
                previousPositions = new PlayerPreviousPositionsCapability();
            };
            return previousPositions;
        };
    
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if(cap == CAPABILITY) {
                return optional.cast();
            };
            return LazyOptional.empty();
        };
    
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            createPlayerPreviousPositions().saveNBTData(tag);
            return tag;
        };
    
        @Override
        public void deserializeNBT(CompoundTag tag) {
            createPlayerPreviousPositions().loadNBTData(tag);
        };
        
    };

    @SubscribeEvent
    public static final void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (!player.level().isClientSide()) player.getCapability(PlayerPreviousPositionsCapability.CAPABILITY).ifPresent((playerPreviousPositions -> {
            playerPreviousPositions.incrementTickCounter();
            if (playerPreviousPositions.hasBeenSecond()) playerPreviousPositions.recordPosition(player.blockPosition());
        }));
    };
    
};
