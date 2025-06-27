package com.petrolpark.destroy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(FluidTransportBehaviour.class)
public class FluidTransportBehaviourMixin {
    private ThreadLocal<FluidStack> accumulatedAvailableFluid = new ThreadLocal<>();

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
            accumulatedAvailableFluid.set(FluidStack.EMPTY);
        } else if(DestroyFluids.isMixture(providedFluid)) {
            if(accumulatedAvailableFluid.get().isEmpty()) {
                // First Mixture input, remember it and carry on as normal
                accumulatedAvailableFluid.set(providedFluid);
                return providedFluid;
            } else {
                // If this pipe segments has multiple Mixture inputs, mix them together
                // This is purely visual for the sake of displaying flowing liquids in transparent pipes
                LegacyMixture existingMixture = LegacyMixture.readNBT(accumulatedAvailableFluid.get().getOrCreateTag().getCompound("Mixture"));
                LegacyMixture addedMixture = LegacyMixture.readNBT(providedFluid.getOrCreateTag().getCompound("Mixture"));
                int existingAmount = accumulatedAvailableFluid.get().getAmount();
                int addedAmount = providedFluid.getAmount();
                LegacyMixture newMixture = LegacyMixture.mix(Map.of(existingMixture, existingAmount/1000d, addedMixture, addedAmount/1000d));

                accumulatedAvailableFluid.set(MixtureFluid.of(existingAmount + addedAmount, newMixture));
                return accumulatedAvailableFluid.get();
            }
        }

        // Not a Mixture input, carry on as normal
        return providedFluid;
    }
}
