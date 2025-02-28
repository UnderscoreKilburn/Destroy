package com.petrolpark.destroy.core.chemistry.storage.measuringcylinder;

import com.petrolpark.destroy.DestroyBlockEntityTypes;
import com.petrolpark.destroy.DestroyVoxelShapes;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.core.chemistry.storage.PlaceableMixtureTankBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MeasuringCylinderBlock extends PlaceableMixtureTankBlock<MeasuringCylinderBlockEntity> {

    public MeasuringCylinderBlock(Properties properties) {
        super(properties);
    };

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return DestroyVoxelShapes.MEASURING_CYLINDER;
    };

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return MeasuringCylinderBlockItem.tryOpenTransferScreen(pLevel, pPos, pState, pHit.getDirection(), pPlayer, pHand, pPlayer.getItemInHand(pHand), dynamicShape);
    };

    @Override
    public int getMixtureCapacity() {
        return DestroyAllConfigs.SERVER.blocks.measuringCylinderCapacity.get();
    };

    @Override
    public Class<MeasuringCylinderBlockEntity> getBlockEntityClass() {
        return MeasuringCylinderBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends MeasuringCylinderBlockEntity> getBlockEntityType() {
        return DestroyBlockEntityTypes.MEASURING_CYLINDER.get();
    };
    
};
