package com.petrolpark.destroy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.petrolpark.destroy.core.fluid.openpipeeffect.FlowControlledOpenPipeEffectHandler;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "com/simibubi/create/content/fluids/OpenEndedPipe$OpenEndFluidHandler")
public class OpenEndFluidHandlerMixin {
    @WrapOperation(
        method= "fill(Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)I",
        at=@At(
            value="INVOKE",
            target="Lcom/simibubi/create/foundation/fluid/FluidHelper;copyStackWithAmount(Lnet/minecraftforge/fluids/FluidStack;I)Lnet/minecraftforge/fluids/FluidStack;",
            remap=false
        ),
        remap=false
    )
    private FluidStack redirectFluidStack(FluidStack fluidStack, int amount, Operation<FluidStack> original, @Local OpenPipeEffectHandler effectHandler) {
        if(effectHandler instanceof FlowControlledOpenPipeEffectHandler flowControlledEffectHandler)
            return FluidHelper.copyStackWithAmount(fluidStack, flowControlledEffectHandler.getFlow(fluidStack));
        else
            return original.call(fluidStack, amount);
    }
}
