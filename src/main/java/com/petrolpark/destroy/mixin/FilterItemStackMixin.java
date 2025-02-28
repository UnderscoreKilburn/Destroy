package com.petrolpark.destroy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.petrolpark.destroy.DestroyFluids;
import com.simibubi.create.content.logistics.filter.FilterItemStack;

import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

@Mixin(FilterItemStack.class)
public class FilterItemStackMixin {

    @Shadow
    private FluidStack filterFluidStack;
    
    @Inject(
        method = "Lcom/simibubi/create/content/logistics/filter/FilterItemStack;test(Lnet/minecraft/world/level/Level;Lnet/minecraftforge/fluids/FluidStack;Z)Z",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    public void inTest(Level level, FluidStack stack, boolean matchNBT, CallbackInfoReturnable<Boolean> cir) {
        if (DestroyFluids.isMixture(stack) && DestroyFluids.isMixture(filterFluidStack)) cir.setReturnValue(!matchNBT);
    };
};
