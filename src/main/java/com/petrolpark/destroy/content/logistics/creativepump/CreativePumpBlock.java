package com.petrolpark.destroy.content.logistics.creativepump;

import com.petrolpark.destroy.DestroyBlockEntityTypes;
import com.petrolpark.destroy.DestroyVoxelShapes;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CreativePumpBlock extends PumpBlock {

    public CreativePumpBlock(Properties properties) {
        super(properties);
    };

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return DestroyVoxelShapes.CREATIVE_PUMP.get(state.getValue(FACING).getAxis());
    };

    @Override
    public BlockEntityType<? extends PumpBlockEntity> getBlockEntityType() {
        return DestroyBlockEntityTypes.CREATIVE_PUMP.get();
    };

    @Override
    public boolean isSmallCog() {
        return false;
    };
    
};
