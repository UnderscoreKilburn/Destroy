package com.petrolpark.destroy.content.processing.dynamo.arcfurnace;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.petrolpark.destroy.DestroyBlockEntityTypes;
import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyVoxelShapes;
import com.petrolpark.destroy.content.processing.dynamo.DynamoBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder; 
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArcFurnaceLidBlock extends Block implements IWrenchable {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public ArcFurnaceLidBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(AXIS, Axis.Z));
    };

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (DestroyBlocks.DYNAMO.has(context.getLevel().getBlockState(context.getClickedPos().above()))) context.getLevel().setBlock(context.getClickedPos().above(), state.cycle(DynamoBlock.AXIS), 3);
        context.getLevel().setBlock(context.getClickedPos(), state.cycle(AXIS), 3);
        return InteractionResult.SUCCESS;
    };

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);
        if (DestroyBlocks.DYNAMO.has(level.getBlockState(pos.above()))) {
            level.setBlock(pos.above(), level.getBlockState(pos.above()).setValue(DynamoBlock.ARC_FURNACE, false), 3);
        };
    };

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return Optional.ofNullable(params.getParameter(LootContextParams.ORIGIN))
            .map(BlockPos::containing)
            .map(pos ->
                params.getLevel().getBlockEntity(pos.above(), DestroyBlockEntityTypes.DYNAMO.get())
                .map(dynamo -> dynamo.arcFurnaceBlock.get().getDrops(params))
                .orElse(Collections.singletonList(ItemStack.EMPTY))
            ).orElse(Collections.singletonList(ItemStack.EMPTY));
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(AXIS);
        super.createBlockStateDefinition(builder);
    };

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return DestroyVoxelShapes.ARC_FURNACE_LID.get(state.getValue(AXIS));
    };

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rotation) {
        return (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) ? state.cycle(AXIS) : state;
    };

};
