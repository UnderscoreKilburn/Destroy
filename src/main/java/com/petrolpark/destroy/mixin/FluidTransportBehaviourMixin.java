package com.petrolpark.destroy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FluidTransportBehaviour.class)
public class FluidTransportBehaviourMixin {
    @Unique
    private final ThreadLocal<FluidStack> destroy$accumulatedAvailableFluid = new ThreadLocal<>();

    @WrapOperation(
        method = "tick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/fluids/FluidStack;isFluidEqual(Lnet/minecraftforge/fluids/FluidStack;)Z",
            remap = false
        ),
        remap = false
    )
    private boolean considerMixturesEqual(FluidStack fluid, FluidStack other, Operation<Boolean> original) {
        if(original.call(fluid, other))
            return true;
        else if(DestroyFluids.isMixture(fluid) && DestroyFluids.isMixture(other)) {
            return true;
        }

        return false;
    }

    @ModifyVariable(
        method = "tick()V",
        name = "availableFlow",
        at = @At("STORE"),
        remap = false
    )
    private FluidStack accumulateAvailableFluidStack(FluidStack providedFluid) {
        if(providedFluid.isEmpty()) {
            // If providedFluid is empty, we just initialized availableFlow
            destroy$accumulatedAvailableFluid.set(FluidStack.EMPTY);
        } else if(DestroyFluids.isMixture(providedFluid)) {
            if(destroy$accumulatedAvailableFluid.get().isEmpty()) {
                // First Mixture input, remember it and carry on as normal
                destroy$accumulatedAvailableFluid.set(providedFluid);
                return providedFluid;
            } else {
                // If this pipe segments has multiple Mixture inputs, mix them together
                // This is purely visual for the sake of displaying flowing liquids in transparent pipes
                LegacyMixture existingMixture = LegacyMixture.readNBT(destroy$accumulatedAvailableFluid.get().getOrCreateTag().getCompound("Mixture"));
                LegacyMixture addedMixture = LegacyMixture.readNBT(providedFluid.getOrCreateTag().getCompound("Mixture"));
                int existingAmount = destroy$accumulatedAvailableFluid.get().getAmount();
                int addedAmount = providedFluid.getAmount();
                LegacyMixture newMixture = LegacyMixture.mix(existingMixture, existingAmount, addedMixture, addedAmount);

                destroy$accumulatedAvailableFluid.set(MixtureFluid.of(existingAmount + addedAmount, newMixture));
                return destroy$accumulatedAvailableFluid.get();
            }
        }

        // Not a Mixture input, carry on as normal
        return providedFluid;
    }
}
