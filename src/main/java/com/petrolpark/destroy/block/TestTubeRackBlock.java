package com.petrolpark.destroy.block;

import java.util.List;

import com.petrolpark.destroy.block.entity.DestroyBlockEntityTypes;
import com.petrolpark.destroy.block.entity.TestTubeRackBlockEntity;
import com.petrolpark.destroy.block.shape.DestroyShapes;
import com.petrolpark.destroy.item.IMixtureStorageItem;
import com.petrolpark.util.RayHelper;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TestTubeRackBlock extends Block implements IBE<TestTubeRackBlockEntity>, IWrenchable, ISpecialMixtureContainerBlock {

    public static final BooleanProperty X = BooleanProperty.create("x");

    public TestTubeRackBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(X, true));
    };

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(X);
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(X, context.getHorizontalDirection().getAxis() == Axis.Z);
    };

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(X) ? DestroyShapes.TEST_TUBE_RACK_X : DestroyShapes.TEST_TUBE_RACK_Z;
    };

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        int tube = getTargetedTube(state, pos, player);
        if (tube == -1) return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);
        return onBlockEntityUse(level, pos, be -> {
            ItemStack oldStack = be.inv.getStackInSlot(tube).copy();
            if (stack.isEmpty() && oldStack.isEmpty()) return InteractionResult.PASS;
            if (!be.inv.isItemValid(tube, stack) && !stack.isEmpty()) return InteractionResult.FAIL;
            be.inv.setStackInSlot(tube, stack.copy());
            stack.shrink(1);
            if (!oldStack.isEmpty()) {
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, oldStack); 
                } else {
                    player.getInventory().placeItemBackInInventory(oldStack);
                };
            };
            return InteractionResult.SUCCESS;
        });
    };

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        withBlockEntityDo(world, pos, be -> ItemHelper.dropContents(be.getLevel(), pos, be.inv));
        super.onRemove(state, world, pos, newState, isMoving);
    }

    /**
     * @param state
     * @param pos
     * @param player
     * @return {@code -1} if there is no collision or {@code 0} to {@code 3} depending on which tube is hit
     */
    public static int getTargetedTube(BlockState state, BlockPos pos, Player player) {
        Vec3 start = player.getEyePosition();
        Vec3 end = player.getEyePosition().add(player.getLookAngle().scale(player.getBlockReach()));
        return RayHelper.getHit(List.of(getTubeBox(state, pos, 0), getTubeBox(state, pos, 1), getTubeBox(state, pos, 2), getTubeBox(state, pos, 3)), start, end);
    };

    public static AABB getTubeBox(BlockState state, BlockPos pos, int tube) {
        if (tube <0 || tube >= 4) return new AABB(0d, 0d, 0d, 0d, 0d, 0d);
        boolean x = state.getValue(X);
        double boxStart = tube * 4 /16d;
        return new AABB(Vec3.atLowerCornerOf(pos).add(x ? boxStart + 0.5 /16d: 6.5 / 16d, 2.1 / 16d, x ? 6.5 / 16d : boxStart + 0.5 / 16d), Vec3.atLowerCornerOf(pos).add(x ? boxStart + 3.5 / 16d: 9.5 / 16d, 10 / 16d, x ? 9.5 / 16d : boxStart + 3.5 / 16d));
    };

    @Override
    public Class<TestTubeRackBlockEntity> getBlockEntityClass() {
        return TestTubeRackBlockEntity.class;
    };

    @Override
    public BlockEntityType<? extends TestTubeRackBlockEntity> getBlockEntityType() {
        return DestroyBlockEntityTypes.TEST_TUBE_RACK.get();
    }

    @Override
    public IFluidHandler getTankForMixtureStorageItems(IMixtureStorageItem item, Level level, BlockPos pos, BlockState state, Direction face, Player player, InteractionHand hand, ItemStack stack, boolean rightClick) {
        TestTubeRackBlockEntity be = getBlockEntity(level, pos);
        if (be == null) return null;
        int tube = getTargetedTube(state, pos, player);
        if (tube == -1) return null;
        ItemStack tubeStack = be.inv.getStackInSlot(tube);
        return tubeStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
    };
    
};
