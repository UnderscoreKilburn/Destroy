package com.petrolpark.destroy.content.processing.treetap;

import com.petrolpark.compat.create.block.entity.behaviour.AbstractRememberPlacerBehaviour;
import com.petrolpark.destroy.DestroyBlockEntityTypes;
import com.petrolpark.destroy.DestroyVoxelShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TreeTapBlock extends HorizontalKineticBlock implements IBE<TreeTapBlockEntity>, ProperWaterloggedBlock {

    public TreeTapBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    };

    @Override
    public FluidState getFluidState(BlockState state) {
        return fluidState(state);
    };

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return DestroyVoxelShapes.TREE_TAP;
    };

    @Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
		updateWater(world, state, pos);
		return state;
	};

    @Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		withBlockEntityDo(worldIn, pos, TreeTapBlockEntity::destroyNextTick);
	};

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return withWater(defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection()), context);
    };

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        AbstractRememberPlacerBehaviour.setPlacedBy(worldIn, pos, placer);
    };

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return (face.getAxis() != Axis.Y && face.getAxis() != state.getValue(HORIZONTAL_FACING).getAxis());
    };

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getClockWise().getAxis();
    };

    @Override
    public Class<TreeTapBlockEntity> getBlockEntityClass() {
        return TreeTapBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends TreeTapBlockEntity> getBlockEntityType() {
        return DestroyBlockEntityTypes.TREE_TAP.get();
    };
    
};
