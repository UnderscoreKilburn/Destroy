package com.petrolpark.destroy.core.fluid.openpipeeffect;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import net.minecraftforge.fluids.FluidStack;

public interface FlowControlledOpenPipeEffectHandler extends OpenPipeEffectHandler {
    int getFlow(FluidStack fluid);
}
