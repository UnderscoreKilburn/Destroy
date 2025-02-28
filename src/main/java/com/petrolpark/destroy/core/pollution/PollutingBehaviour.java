package com.petrolpark.destroy.core.pollution;

import java.util.ArrayList;
import java.util.List;

import com.petrolpark.destroy.Destroy;
import com.simibubi.create.api.event.BlockEntityBehaviourEvent;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * Behaviour for Tile Entities which contain Fluids and should release those Fluids into the atmosphere if destroyed.
 */
@EventBusSubscriber(modid = Destroy.MOD_ID)
public class PollutingBehaviour extends BlockEntityBehaviour {

    public static BehaviourType<PollutingBehaviour> TYPE = new BehaviourType<>();

    public PollutingBehaviour(SmartBlockEntity be) {
        super(be);
    };

    @Override
    public void destroy() {
        List<FluidStack> fluidsToRelease = new ArrayList<>();
        IFluidHandler availableFluids = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
        if (availableFluids == null) return;
        for (int tankNo = 0; tankNo < availableFluids.getTanks(); tankNo++) {
            FluidStack fluidStack = availableFluids.getFluidInTank(tankNo);
            if (fluidStack.isEmpty()) continue;
            fluidsToRelease.add(fluidStack);
        };

        PollutionHelper.pollute(getWorld(), getPos(), fluidsToRelease.toArray((i) -> new FluidStack[i]));

        super.destroy();
    };

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    };

    @SubscribeEvent
    public static void onAttachBasinBehaviours(BlockEntityBehaviourEvent<BasinBlockEntity> event) {
        event.attach(new PollutingBehaviour(event.getBlockEntity()));
    };

    @SubscribeEvent
    public static void onAttachDrainBehaviours(BlockEntityBehaviourEvent<ItemDrainBlockEntity> event) {
        event.attach(new PollutingBehaviour(event.getBlockEntity()));
    };

    @SubscribeEvent
    public static void onAttachSpoutBehaviours(BlockEntityBehaviourEvent<SpoutBlockEntity> event) {
        event.attach(new PollutingBehaviour(event.getBlockEntity()));
    };
    
};
