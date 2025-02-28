package com.petrolpark.destroy.content.processing.moltenblock;

import com.petrolpark.destroy.DestroyBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BorosilicateGlassFiberBlock extends FastCoolingMoltenPillarBlock {

    public BorosilicateGlassFiberBlock(Properties properties) {
        super(properties);
    };

    @Override
    @SuppressWarnings("deprecation")
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction side) {
        return (state.getValue(MOLTEN) && (DestroyBlocks.MOLTEN_BOROSILICATE_GLASS.has(adjacentState) || (DestroyBlocks.BOROSILICATE_GLASS_FIBER.has(adjacentState) && adjacentState.getValue(FastCoolingMoltenPillarBlock.MOLTEN)))) || super.skipRendering(state, adjacentState, side);
    };

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    };
  
    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1f;
    };
  
    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    };
    
};
