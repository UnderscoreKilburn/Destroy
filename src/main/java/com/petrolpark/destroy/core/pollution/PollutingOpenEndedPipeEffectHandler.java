package com.petrolpark.destroy.core.pollution;

import java.util.Random;

import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyMessages;
import com.petrolpark.destroy.core.fluid.gasparticle.EvaporatingFluidS2CPacket;
import com.petrolpark.destroy.core.fluid.openpipeeffect.FlowControlledOpenPipeEffectHandler;
import com.petrolpark.destroy.core.pollution.Pollution.PollutionType;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

public class PollutingOpenEndedPipeEffectHandler implements FlowControlledOpenPipeEffectHandler {

    private static final Random random = new Random();

    @SuppressWarnings("deprecation")
    public boolean canApplyEffects(FluidStack fluid) {
        if (DestroyFluids.isMixture(fluid)) return true;
        for (PollutionType pollutionType : PollutionType.values()) {
            if (fluid.getFluid().is(pollutionType.fluidTag)) return true;
        };
        return false;
    };

    @Override
    public void apply(Level level, AABB area, FluidStack fluid) {
        if(!canApplyEffects(fluid)) return;
        BlockPos pollutionAt = new BlockPos((int) area.minX, (int) area.maxY - 1, (int) area.minZ);
        PollutionHelper.pollute(level, pollutionAt, fluid);
        if (random.nextInt(20) == 0) DestroyMessages.sendToAllClients(new EvaporatingFluidS2CPacket(pollutionAt, fluid));
    }

    @Override
    public int getFlow(FluidStack fluid) {
        // Create restricts flow to 1 mB/tick when a fluid spilled through an open pipe has an associated OpenPipeEffectHandler - we don't want this when spilling mixtures
        return fluid.getAmount();
    }
};
