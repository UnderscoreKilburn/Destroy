package com.petrolpark.destroy.mixin;

import java.util.Iterator;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.petrolpark.destroy.content.processing.ISpecialAirCurrentBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;

import net.minecraft.world.level.Level;

@Mixin(AirCurrent.class)
public class AirCurrentMixin {
    
    @Inject(
        method = "Lcom/simibubi/create/content/kinetics/fan/AirCurrent;tickAffectedHandlers()V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/simibubi/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour;handleProcessingOnAllItems(Ljava/util/function/Function;)V"
        ),
        remap = false,
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    @SuppressWarnings("rawtypes")
    public void inTickAffectedHandlers(CallbackInfo ci, Iterator var1, Pair pair, TransportedItemStackHandlerBehaviour handler, Level world, FanProcessingType processingType) {
        if (handler instanceof ISpecialAirCurrentBehaviour special) special.tickAir((AirCurrent)(Object)this, processingType);
    };
};
