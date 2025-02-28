package com.petrolpark.destroy.content.processing.treetap;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.petrolpark.destroy.DestroyAdvancementTrigger;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.core.data.advancement.DestroyAdvancementBehaviour;
import com.petrolpark.destroy.core.fluid.GeniusFluidTankBehaviour;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.BlockHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TreeTapBlockEntity extends BlockBreakingKineticBlockEntity {

    protected LazyOptional<IFluidHandler> fluidCapability;
	public GeniusFluidTankBehaviour tank;

    protected DestroyAdvancementBehaviour advancementBehaviour;

    protected BlockTapping currentTapping;

    public TreeTapBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    };

    @Override
    protected BlockPos getBreakingPos() {
        return getBlockPos().relative(getBlockState().getValue(TreeTapBlock.HORIZONTAL_FACING)).above();
    };

    @Override
    public boolean canBreak(BlockState stateToBreak, float blockHardness) {
        if (!super.canBreak(stateToBreak, blockHardness)) return false;
        if (currentTapping == null || !currentTapping.tappable.test(stateToBreak)) {
            currentTapping = null;
            for (BlockTapping tapping : BlockTapping.ALL_TAPPINGS) {
                if (tapping.tappable.test(stateToBreak)) currentTapping = tapping;
                break;
            };
        };
        return currentTapping != null && tank.getPrimaryHandler().fill(currentTapping.result, FluidAction.SIMULATE) > 0;
    };

    @Override
    public void onBlockBroken(BlockState stateToBreak) {
        BlockHelper.destroyBlock(level, breakingPos, 1f, stack -> {}); // Don't drop items
        tank.getPrimaryHandler().fill(currentTapping.result, FluidAction.EXECUTE);
        advancementBehaviour.awardDestroyAdvancement(DestroyAdvancementTrigger.TAP_TREE);
    };

    @Override
    protected float getBreakSpeed() {
        return super.getBreakSpeed() / 32f;
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        tank = new GeniusFluidTankBehaviour(GeniusFluidTankBehaviour.TYPE, this, 1, getCapacity(), false);
        tank.forbidInsertion();
        behaviours.add(tank);

        advancementBehaviour = new DestroyAdvancementBehaviour(this, DestroyAdvancementTrigger.TAP_TREE);
        behaviours.add(advancementBehaviour);

        refreshCapability();
    };

    private void refreshCapability() {
		LazyOptional<IFluidHandler> oldCap = fluidCapability;
		fluidCapability = LazyOptional.of(tank::getPrimaryHandler);
		if (oldCap != null) oldCap.invalidate();
	};

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (fluidCapability == null) return false;
        return containedFluidTooltip(tooltip, isPlayerSneaking, fluidCapability);
    };

    @Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side != null && side != getBlockState().getValue(TreeTapBlock.HORIZONTAL_FACING).getOpposite()) return super.getCapability(cap, side);
		if (!fluidCapability.isPresent()) refreshCapability();
		if (cap == ForgeCapabilities.FLUID_HANDLER) return fluidCapability.cast();
		return super.getCapability(cap, side);
	};

    public int getCapacity() {
        return DestroyAllConfigs.SERVER.blocks.treeTapCapacity.get();
    };
    
};
