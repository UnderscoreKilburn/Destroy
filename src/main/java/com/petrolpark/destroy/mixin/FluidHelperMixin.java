package com.petrolpark.destroy.mixin;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.JsonOps;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FluidHelper.class)
public class FluidHelperMixin {
    /**
     * Write fluid NBT data as a JSON element rather than a string.
     * This isn't really necessary but it's more human readable that way.
     */
    @WrapOperation(
        method = "Lcom/simibubi/create/foundation/fluid/FluidHelper;serializeFluidStack(Lnet/minecraftforge/fluids/FluidStack;)Lcom/google/gson/JsonElement;",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/gson/JsonObject;addProperty(Ljava/lang/String;Ljava/lang/String;)V"
        ),
        remap = false
    )
    private static void writeFancyNBT(JsonObject json, String property, String value, Operation<Void> original, @Local(argsOnly = true) FluidStack stack) {
        if(property.equals("nbt")) {
            CompoundTag tag = stack.getTag().copy();
            if(tag.contains("Mixture", CompoundTag.TAG_COMPOUND))
                tag.getCompound("Mixture").remove("AtEquilibrium");

            json.add(property, NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, tag));
        } else {
            original.call(json, property, value);
        }
    }

}
