package com.petrolpark.destroy.core.chemistry.storage;

import com.petrolpark.destroy.DestroyBlockEntityTypes;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;

public class ElementTankBlock extends HorizontalDirectionalBlock implements IBE<ElementTankBlockEntity> {

    public ElementTankBlock(Properties properties) {
        super(properties);
    };

    @Override
    public Class<ElementTankBlockEntity> getBlockEntityClass() {
        return ElementTankBlockEntity.class;
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    };

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        IBE.onRemove(pState, pLevel, pPos, pState);
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    };

    @Override
    public BlockEntityType<? extends ElementTankBlockEntity> getBlockEntityType() {
        return DestroyBlockEntityTypes.ELEMENT_TANK.get();
    };
    
    
};
