package com.petrolpark.destroy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.petrolpark.destroy.core.fluid.GeniusFluidTankBehaviour;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemDrainBlockEntity.class)
public class ItemDrainBlockEntityMixin {
    @WrapOperation(method = "addBehaviours", at = @At(
        value = "INVOKE",
        target = "Lcom/simibubi/create/foundation/blockEntity/behaviour/fluid/SmartFluidTankBehaviour;single(Lcom/simibubi/create/foundation/blockEntity/SmartBlockEntity;I)Lcom/simibubi/create/foundation/blockEntity/behaviour/fluid/SmartFluidTankBehaviour;",
        remap = false
    ), remap = false)
    SmartFluidTankBehaviour replaceFluidTank(SmartBlockEntity be, int capacity, Operation<SmartFluidTankBehaviour> original) {
        return new GeniusFluidTankBehaviour(SmartFluidTankBehaviour.TYPE, be, 1, capacity, false);
    }
}
