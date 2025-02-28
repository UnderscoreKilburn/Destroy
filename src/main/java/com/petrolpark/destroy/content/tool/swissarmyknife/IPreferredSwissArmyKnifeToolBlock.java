package com.petrolpark.destroy.content.tool.swissarmyknife;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IPreferredSwissArmyKnifeToolBlock {

    /**
     * Get the tool to which a Swiss Army Knife should switch if this block is targeted.
     * This is only called client-side.
     * @param level
     * @param pos
     * @param state
     * @param shiftDown Whether the Swiss Army Knife's user has shift down - if this is {@code false}, this is typically the tool used for mining
     */
    public SwissArmyKnifeItem.Tool getToolForSwissArmyKnife(Level level, BlockPos pos, BlockState state, boolean shiftDown);
};
