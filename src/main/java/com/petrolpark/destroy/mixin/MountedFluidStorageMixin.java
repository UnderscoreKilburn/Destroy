package com.petrolpark.destroy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.MountedFluidStorage;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.petrolpark.destroy.core.fluid.GeniusFluidTankBehaviour.GeniusFluidTank;

import java.util.function.Consumer;

@Mixin(MountedFluidStorage.class)
public abstract class MountedFluidStorageMixin {
    @Inject(
        method = "Lcom/simibubi/create/content/contraptions/MountedFluidStorage;createMountedTank(Lnet/minecraft/world/level/block/entity/BlockEntity;)Lcom/simibubi/create/foundation/fluid/SmartFluidTank;",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private void createGeniusMountedTank(BlockEntity be, CallbackInfoReturnable<SmartFluidTank> cir) {
        if(be instanceof FluidTankBlockEntity tank) {
            cir.setReturnValue(new GeniusFluidTank(
                tank.getTotalTankSize() * FluidTankBlockEntity.getCapacityMultiplier(),
                this::invokeOnFluidStackChanged));
        }
    }

    @WrapOperation(
        method = "Lcom/simibubi/create/content/contraptions/MountedFluidStorage;deserialize(Lnet/minecraft/nbt/CompoundTag;)Lcom/simibubi/create/content/contraptions/MountedFluidStorage;",
        at = @At(
            value = "NEW",
            target = "com/simibubi/create/foundation/fluid/SmartFluidTank"
        ),
        remap = false
    )
    private static SmartFluidTank deserializeGeniusMountedTank(int capacity, Consumer<FluidStack> updateCallback, Operation<SmartFluidTank> original) {
        return new GeniusFluidTank(capacity, updateCallback);
    }

    @Invoker(
        value = "onFluidStackChanged",
        remap = false
    )
    public abstract void invokeOnFluidStackChanged(FluidStack stack);
}
