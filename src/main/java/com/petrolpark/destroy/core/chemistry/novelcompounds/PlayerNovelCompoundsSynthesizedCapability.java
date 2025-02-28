package com.petrolpark.destroy.core.chemistry.novelcompounds;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.petrolpark.destroy.DestroyStats;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerNovelCompoundsSynthesizedCapability {

    public static final Capability<PlayerNovelCompoundsSynthesizedCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerNovelCompoundsSynthesizedCapability>() {});
  
    protected Set<String> novelCompoundFROWNSStrings = new HashSet<>();

    public static void add(Player player, LegacySpecies novelCompound) {
        player.getCapability(CAPABILITY).ifPresent(pncs -> {
            if (pncs.novelCompoundFROWNSStrings.add(novelCompound.getFROWNSCode())) player.awardStat(DestroyStats.NOVEL_COMPOUNDS_SYNTHESIZED.get());
        });
    };

    public void copyFrom(PlayerNovelCompoundsSynthesizedCapability pncs) {
        novelCompoundFROWNSStrings.clear();
        novelCompoundFROWNSStrings.addAll(pncs.novelCompoundFROWNSStrings);
    };

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        private PlayerNovelCompoundsSynthesizedCapability playerNovelCompounds = null;
        private final LazyOptional<PlayerNovelCompoundsSynthesizedCapability> optional = LazyOptional.of(this::createPlayerNovelCompoundsSynthesized);

        private PlayerNovelCompoundsSynthesizedCapability createPlayerNovelCompoundsSynthesized() {
            if (playerNovelCompounds == null) {
                playerNovelCompounds = new PlayerNovelCompoundsSynthesizedCapability();
            };
            return playerNovelCompounds;
        };

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            ListTag listTag = new ListTag();
            createPlayerNovelCompoundsSynthesized().novelCompoundFROWNSStrings.forEach(string -> listTag.add(StringTag.valueOf(string)));
            tag.put("FROWNSStrings", listTag);
            return tag;
        };

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            createPlayerNovelCompoundsSynthesized().novelCompoundFROWNSStrings = nbt.getList("FROWNSStrings", Tag.TAG_STRING).stream().map(Tag::getAsString).collect(HashSet::new, Set::add, Set::addAll);
        };

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if(cap == CAPABILITY) return optional.cast();
            return LazyOptional.empty();
        };

    };
};
