package com.petrolpark.destroy.mixin;

import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.core.chemistry.vat.VatSideBlockEntity;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchBlockEntity;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchObservable;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThresholdSwitchBlockEntity.class)
public abstract class ThresholdSwitchBlockEntityMixin extends SmartBlockEntity {

    @Shadow
    abstract BlockPos getTargetPos();

    @Inject(method = "getDisplayItemForScreen", at = @At("HEAD"), cancellable = true, remap = false)
    void overrideDisplayItem(CallbackInfoReturnable<ItemStack> cir) {
        if(level.getBlockEntity(getTargetPos()) instanceof VatSideBlockEntity)
            cir.setReturnValue(DestroyBlocks.VAT_CONTROLLER.asStack());
    }

    @Inject(method = "getTypeOfCurrentTarget", at = @At("HEAD"), cancellable = true, remap = false)
    void overrideTargetType(CallbackInfoReturnable<ThresholdSwitchBlockEntity.ThresholdType> cir) {
        // go figure why this check isn't done first in the original function
        if(level.getBlockEntity(getTargetPos()) instanceof ThresholdSwitchObservable)
            cir.setReturnValue(ThresholdSwitchBlockEntity.ThresholdType.CUSTOM);
    }

    public ThresholdSwitchBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
