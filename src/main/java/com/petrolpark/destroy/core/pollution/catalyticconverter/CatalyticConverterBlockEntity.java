package com.petrolpark.destroy.core.pollution.catalyticconverter;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.petrolpark.destroy.DestroyAdvancementTrigger;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.core.data.advancement.DestroyAdvancementBehaviour;
import com.petrolpark.destroy.core.fluid.GeniusFluidTankBehaviour;
import com.petrolpark.destroy.core.pollution.PollutionHelper;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class CatalyticConverterBlockEntity extends SmartBlockEntity {

    protected DestroyAdvancementBehaviour advancementBehaviour;
    protected GeniusFluidTankBehaviour tankBehaviour;
    protected int ticksToFlush;

    public CatalyticConverterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    };

    @Override
    public void tick() {
        super.tick();
        ticksToFlush--;
        if (ticksToFlush <= 0) {
            ticksToFlush = 10;
            if (tankBehaviour.isEmpty()) return;
            float multiplier = DestroyAllConfigs.SERVER.blocks.catalyticConverterReduction.getF();
            if (multiplier > 0f) PollutionHelper.pollute(level, getBlockPos().relative(getBlockState().getValue(CatalyticConverterBlock.FACING)), multiplier, 10, tankBehaviour.getPrimaryHandler().getFluid());
            advancementBehaviour.awardDestroyAdvancement(DestroyAdvancementTrigger.CATALYTIC_CONVERTER);
            tankBehaviour.getPrimaryHandler().drain(1000000, FluidAction.EXECUTE);
            notifyUpdate();
        };
    };

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        ticksToFlush = tag.getInt("TicksToFlush");
    };

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putInt("TicksToFlush", ticksToFlush);
    };

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tankBehaviour = new GeniusFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 1000000, false);
        tankBehaviour.forbidExtraction();
        behaviours.add(tankBehaviour);

        advancementBehaviour = new DestroyAdvancementBehaviour(this, DestroyAdvancementTrigger.CATALYTIC_CONVERTER);
        behaviours.add(advancementBehaviour);
    };

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && side == null || side == getBlockState().getValue(CatalyticConverterBlock.FACING).getOpposite()) return tankBehaviour.getCapability().cast();
        return super.getCapability(cap, side);
    };
    
};
