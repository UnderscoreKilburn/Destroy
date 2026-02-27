package com.petrolpark.destroy.mixin.compat.create_connected;

import com.hlysine.create_connected.content.fluidvessel.FluidVesselBlockEntity;
import com.petrolpark.destroy.core.fluid.GeniusFluidTankBehaviour.GeniusFluidTank;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FluidVesselBlockEntity.class)
public abstract class FluidVesselBlockEntityMixin {
    
    @Overwrite(remap = false)
    protected SmartFluidTank createInventory() {
        return new GeniusFluidTank(FluidTankBlockEntity.getCapacityMultiplier(), this::invokeOnFluidStackChanged);
    };

    @Invoker(
        value = "onFluidStackChanged",
        remap = false
    )
    public abstract void invokeOnFluidStackChanged(FluidStack stack);
};
