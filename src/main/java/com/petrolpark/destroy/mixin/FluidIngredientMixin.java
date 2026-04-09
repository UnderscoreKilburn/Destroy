package com.petrolpark.destroy.mixin;

import java.util.Map.Entry;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;

import com.google.gson.JsonObject;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.MixtureFluidIngredient;
import com.petrolpark.destroy.core.recipe.ingredient.fluid.MixtureFluidIngredientSubType;
import com.petrolpark.destroy.mixin.accessor.FluidIngredientAccessor;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidIngredient.class)
public abstract class FluidIngredientMixin {

	// Make {@link com.petrolpark.destroy.core.recipe.ingredient.fluid.MoleculeFluidIngredient Molecule ingredients} valid
	@WrapOperation(method="isFluidIngredient", at=@At(value="INVOKE", target="Lcom/google/gson/JsonObject;has(Ljava/lang/String;)Z", ordinal=0), remap=false)
	private static boolean matchFluidIngredient(JsonObject json, String name, Operation<Boolean> original) {
		if (MixtureFluidIngredient.MIXTURE_FLUID_INGREDIENT_SUBTYPES.keySet().stream().anyMatch(json::has)) {
			return true;
		}
		return original.call(json, name);
	}

	@ModifyVariable(method="deserialize", name="ingredient", at=@At(value = "STORE"), remap=false)
	private static FluidIngredient replaceFluidIngredient(FluidIngredient original, @Local(name="json") JsonObject json) {
		// Deserialize Molecule-involving ingredients
		for (Entry<String, MixtureFluidIngredientSubType<?>> mixtureFluidIngredientType : MixtureFluidIngredient.MIXTURE_FLUID_INGREDIENT_SUBTYPES.entrySet()) {
			if (json.has(mixtureFluidIngredientType.getKey())) {
				return mixtureFluidIngredientType.getValue().getNew();
			}
		}

		return original;
	}

	/*
	* Write an ingredient to the network.
	* If the ingredient is using a custom type, write its type as a string then write the rest of its data ourselves,
	* otherwise write an empty string then let Create handle the rest.
	* */
	@Inject(method="write", at=@At("HEAD"), cancellable=true, remap=false)
	private void writeIngredient(FriendlyByteBuf buffer, CallbackInfo ci) {
		FluidIngredient $this = (FluidIngredient)(Object)this;

		if ($this instanceof MixtureFluidIngredient<?> mixtureFluid) {
			buffer.writeUtf(mixtureFluid.getType().getMixtureFluidIngredientSubtype());
			buffer.writeVarInt(((FluidIngredientAccessor)this).getAmountRequired());
			((FluidIngredientAccessor)this).invokeWriteInternal(buffer);
			ci.cancel();
		} else {
			buffer.writeUtf("");
		}
	}

	/*
	* Read an ingredient from the network.
	* Read a string, if the string is not empty, this is a custom ingredient, handle it ourselves.
	* If the string is empty, this is a standard ingredient, let Create handle it.
	* */
	@Inject(method="read", at=@At("HEAD"), cancellable=true, remap=false)
	private static void readIngredient(FriendlyByteBuf buffer, CallbackInfoReturnable<FluidIngredient> cir) {
		String ingredientType = buffer.readUtf();
		if(ingredientType.isEmpty()) return;

		FluidIngredient ingredient = MixtureFluidIngredient.MIXTURE_FLUID_INGREDIENT_SUBTYPES.get(ingredientType).getNew();
		((FluidIngredientAccessor)ingredient).setAmountRequired(buffer.readVarInt());
		((FluidIngredientAccessor)ingredient).invokeReadInternal(buffer);
		cir.setReturnValue(ingredient);
	}
}
