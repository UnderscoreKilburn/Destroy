package com.petrolpark.destroy.core.explosion.mixedexplosive;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DyeableMixedExplosiveBlockColor implements BlockColor {

    public static final DyeableMixedExplosiveBlockColor INSTANCE = new DyeableMixedExplosiveBlockColor();

    @Override
    public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
        if (tintIndex != 0 || level == null || pos == null) return -1;
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof IDyeableMixedExplosiveBlockEntity dyeableBE)) return -1;
        return dyeableBE.getColor();
    };
    
};
