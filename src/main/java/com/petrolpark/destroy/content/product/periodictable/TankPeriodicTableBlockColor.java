package com.petrolpark.destroy.content.product.periodictable;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TankPeriodicTableBlockColor implements BlockColor {

    public static final TankPeriodicTableBlockColor INSTANCE = new TankPeriodicTableBlockColor();

    @Override
    public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
        if (tintIndex != 0 || level == null || pos == null) return -1;
        Block block = state.getBlock();
        if (!(block instanceof TankPeriodicTableBlock tankBlock)) return -1;
        return tankBlock.color;
    };
    
};
