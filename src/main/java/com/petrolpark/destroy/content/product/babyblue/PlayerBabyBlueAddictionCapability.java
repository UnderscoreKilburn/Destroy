package com.petrolpark.destroy.content.product.babyblue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.petrolpark.destroy.config.DestroyAllConfigs;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerBabyBlueAddictionCapability {

    public static final Capability<PlayerBabyBlueAddictionCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerBabyBlueAddictionCapability>() { });

    private int babyBlueAddiction;

    public void copyFrom(PlayerBabyBlueAddictionCapability source) {
        this.babyBlueAddiction = source.babyBlueAddiction;
    };

    public int getBabyBlueAddiction() {
        return this.babyBlueAddiction;
    };

    public void setBabyBlueAddiction(int babyBlueAddiction) {
        this.babyBlueAddiction = Mth.clamp(babyBlueAddiction, 0, getMaxBabyBlueAddiction());
    };

    public static final int getMaxBabyBlueAddiction() {
        return DestroyAllConfigs.SERVER.substances.babyBlueMaxAddictionLevel.get();
    };

    public void addBabyBlueAddiction(int change) {
        this.babyBlueAddiction = Mth.clamp(this.babyBlueAddiction + change, 0, getMaxBabyBlueAddiction());
    };

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("BabyBlueAddiction", this.babyBlueAddiction);
    };

    public void loadNBTData(CompoundTag nbt) {
        this.babyBlueAddiction = nbt.getInt("BabyBlueAddiction");
    };

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        private PlayerBabyBlueAddictionCapability babyBlueAddiction = null;
        private final LazyOptional<PlayerBabyBlueAddictionCapability> optional = LazyOptional.of(this::createPlayerBabyBlueAddiction);

        private PlayerBabyBlueAddictionCapability createPlayerBabyBlueAddiction() {
            if (babyBlueAddiction == null) babyBlueAddiction = new PlayerBabyBlueAddictionCapability();
            return this.babyBlueAddiction;
        };

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == CAPABILITY) return optional.cast();
            return LazyOptional.empty();
        };
        
        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            createPlayerBabyBlueAddiction().saveNBTData(nbt);
            return nbt;
        };

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            createPlayerBabyBlueAddiction().loadNBTData(nbt);
        };
    };
}
