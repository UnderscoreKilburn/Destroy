package com.petrolpark.destroy.mixin;

import java.util.Iterator;

import com.petrolpark.destroy.block.DestroyBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.mixin.accessor.FluidNetworkAccessor;
import com.simibubi.create.content.fluids.FluidNetwork;
import com.simibubi.create.content.fluids.PipeConnection;
import com.simibubi.create.content.fluids.PipeConnection.Flow;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Pair;

import net.minecraftforge.fluids.FluidStack;

@Mixin(FluidNetwork.class)
public class FluidNetworkMixin {
    @Redirect(
            method="Lcom/simibubi/create/content/fluids/FluidNetwork;tick()V",
            at=@At(
                    value="INVOKE",
                    ordinal=0,
                    target="Lnet/minecraftforge/fluids/FluidStack;isFluidEqual(Lnet/minecraftforge/fluids/FluidStack;)Z"
            ),
            remap=false
    )
    private boolean considerMixturesEqual(FluidStack fluid, FluidStack other) {
        if(fluid.isFluidEqual(other))
            return true;
        else if(DestroyFluids.isMixture(fluid) && DestroyFluids.isMixture(other)) {
            return true;
        }

        return false;
    }

    /**
     * Similar to {@link com.petrolpark.destroy.mixin.PipeConnectionMixin here}, we don't want a Fluid Network to reset transferring Fluid if all that has changed
     * is one Mixture becoming another.
     */
    /*
    @Inject(
        method = "Lcom/simibubi/create/content/fluids/FluidNetwork;tick()V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Iterator;remove()V",
            ordinal = 1
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILSOFT,
        remap = false

    )
    public void inTick(CallbackInfo ci, int cycle, boolean shouldContinue, Iterator<Pair<BlockFace, PipeConnection>> iterator, Pair<BlockFace, PipeConnection> pair, BlockFace blockFace, PipeConnection pipeConnection, Flow flow) {
        FluidStack fluid = ((FluidNetworkAccessor)this).getFluid();
        if (DestroyFluids.isMixture(fluid) && DestroyFluids.isMixture(flow.fluid)) {
            ((FluidNetworkAccessor)this).setFluid(flow.fluid);
            ci.cancel();
        };
    };*/
};
