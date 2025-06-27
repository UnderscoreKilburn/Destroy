package com.petrolpark.destroy.mixin.compat.supplementaries;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.petrolpark.destroy.core.pollution.PollutionHelper;
import net.mehvahdjukaar.moonlight.api.fluids.forge.SoftFluidStackImpl;
import net.mehvahdjukaar.supplementaries.common.block.faucet.FluidOffer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net/mehvahdjukaar/supplementaries/common/block/faucet/SpongeInteraction")
public class SpongeInteractionMixin {
    @WrapMethod(
        method = "fill",
        remap = false
    )
    public Integer fill(Level level, BlockPos pos, BlockState target, FluidOffer offer, Operation<Integer> original) {
        Integer result = original.call(level, pos, target, offer);
        if(result != null && result.intValue() > 0) {
            // Voiding fluids by pouring them onto a sponge releases them into the atmosphere
            FluidStack fluidStack = SoftFluidStackImpl.toForgeFluid(offer.fluid().copyWithCount(result.intValue()));
            PollutionHelper.pollute(level, pos, 1.f, 1, fluidStack);
        };
        return result;
    }
}
