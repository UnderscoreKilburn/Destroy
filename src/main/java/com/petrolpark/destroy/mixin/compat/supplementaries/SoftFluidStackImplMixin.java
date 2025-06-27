package com.petrolpark.destroy.mixin.compat.supplementaries;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.forge.SoftFluidStackImpl;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SoftFluidStackImpl.class)
public class SoftFluidStackImplMixin {
    @WrapMethod(
        method = "Lnet/mehvahdjukaar/moonlight/api/fluids/forge/SoftFluidStackImpl;toForgeFluid(Lnet/mehvahdjukaar/moonlight/api/fluids/SoftFluidStack;)Lnet/minecraftforge/fluids/FluidStack;",
        remap = false
    )
    private static FluidStack toForgeFluid(SoftFluidStack softFluid, Operation<FluidStack> original) {
        FluidStack stack = original.call(softFluid);
        if(softFluid.hasTag() && softFluid.getTag().contains("Mixture"))
            stack.getOrCreateTag().put("Mixture", softFluid.getTag().getCompound("Mixture"));
        return stack;
    }
}
