package com.petrolpark.destroy.mixin.compat.create_connected;

import com.hlysine.create_connected.content.fluidvessel.FluidVesselMountedStorage;
import com.petrolpark.destroy.core.fluid.GeniusFluidTankBehaviour.GeniusFluidTank;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FluidVesselMountedStorage.Handler.class)
public abstract class FluidVesselMountedStorageHandlerMixin extends FluidTank {

    public FluidVesselMountedStorageHandlerMixin(int capacity) {
        super(capacity);
    };

    @Unique
    public Lazy<GeniusFluidTank> geniusFluidTank = Lazy.of(() -> new GeniusFluidTank(this.capacity, f -> {}));

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        GeniusFluidTank tank = geniusFluidTank.get();
        tank.setFluid(fluid);
        int filled = tank.fill(resource, action);

        if(filled > 0) {
            fluid = tank.getFluid();
            onContentsChanged();
        };

        return filled;
    };

    // This fixes a bug with Create causing an IllegalStateException to be thrown when emptying a contraption holding a fluid with NBT data
    @Override
    public @NotNull FluidStack getFluid() {
        return this.fluid.isEmpty() ? FluidStack.EMPTY : this.fluid;
    };

}
