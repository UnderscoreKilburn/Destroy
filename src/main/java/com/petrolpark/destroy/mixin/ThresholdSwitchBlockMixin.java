package com.petrolpark.destroy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchBlock;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchObservable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThresholdSwitchBlock.class)
public class ThresholdSwitchBlockMixin {
    @Unique
    @SuppressWarnings("null")
    private static final LazyOptional<Object> dummy_optional = LazyOptional.of(() -> null);

    /*
    * Make threshold switches automatically orient themselves towards an adjacent ThresholdSwitchObservable block when placed.
    * This is an obvious oversight and should probably be PR'd to Create but whatever, I'm doing this for now.
    * */
    @WrapOperation(method = "getStateForPlacement", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/block/entity/BlockEntity;getCapability(Lnet/minecraftforge/common/capabilities/Capability;)Lnet/minecraftforge/common/util/LazyOptional;",
        ordinal = 0))
    LazyOptional<?> pretendValidContainer(BlockEntity instance, Capability<?> capability, Operation<LazyOptional<?>> original) {
        if(instance instanceof ThresholdSwitchObservable) {
            // We just want isPresent() to return true for the returned value
            return dummy_optional;
        }
        return original.call(instance, capability);
    }
}
