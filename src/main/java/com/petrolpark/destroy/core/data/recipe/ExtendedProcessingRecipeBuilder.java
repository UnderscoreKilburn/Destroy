package com.petrolpark.destroy.core.data.recipe;

import com.simibubi.create.api.data.recipe.DatagenMod;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A very cool and definitely not hacky wrapper for com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
 * that can be extended with extra functions for recipes with parameters not covered by ProcessingRecipeParams.
 */
public abstract class ExtendedProcessingRecipeBuilder<T extends ProcessingRecipe<?>, B extends ExtendedProcessingRecipeBuilder<T, B>> {
	protected ProcessingRecipeBuilder<T> internal;
	protected List<ICondition> recipeConditions;

	public ExtendedProcessingRecipeBuilder(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, ResourceLocation recipeId) {
		internal = new ProcessingRecipeBuilder<T>(factory, recipeId);
		recipeConditions = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	protected B self() {return (B)this;}

	public B withItemIngredients(Ingredient... ingredients) {
		internal.withItemIngredients(ingredients);
		return self();
	}

	public B withItemIngredients(NonNullList<Ingredient> ingredients) {
		internal.withItemIngredients(ingredients);
		return self();
	}

	public B withSingleItemOutput(ItemStack output) {
		internal.withSingleItemOutput(output);
		return self();
	}

	public B withItemOutputs(ProcessingOutput... outputs) {
		internal.withItemOutputs(outputs);
		return self();
	}

	public B withItemOutputs(NonNullList<ProcessingOutput> outputs) {
		internal.withItemOutputs(outputs);
		return self();
	}

	public B withFluidIngredients(FluidIngredient... ingredients) {
		return withFluidIngredients(NonNullList.of(FluidIngredient.EMPTY, ingredients));
	}

	public B withFluidIngredients(NonNullList<FluidIngredient> ingredients) {
		internal.withFluidIngredients(ingredients);
		return self();
	}

	public B withFluidOutputs(FluidStack... outputs) {
		return withFluidOutputs(NonNullList.of(FluidStack.EMPTY, outputs));
	}

	public B withFluidOutputs(NonNullList<FluidStack> outputs) {
		internal.withFluidOutputs(outputs);
		return self();
	}

	public B duration(int ticks) {
		internal.duration(ticks);
		return self();
	}

	public B averageProcessingDuration() {
		return duration(100);
	}

	public B requiresHeat(HeatCondition condition) {
		internal.requiresHeat(condition);
		return self();
	}

	public abstract void build(T recipe);

	public void build(Consumer<FinishedRecipe> consumer) {
		T recipe = internal.build();
		build(recipe);
		consumer.accept(new ProcessingRecipeBuilder.DataGenResult<>(recipe, recipeConditions));
	}

	// Datagen shortcuts

	public B require(TagKey<Item> tag) {
		return require(Ingredient.of(tag));
	}

	public B require(ItemLike item) {
		return require(Ingredient.of(item));
	}

	public B require(Ingredient ingredient) {
		internal.require(ingredient);
		return self();
	}

	public B require(DatagenMod mod, String id) {
		internal.require(mod, id);
		return self();
	}

	public B require(ResourceLocation ingredient) {
		internal.require(ingredient);
		return self();
	}

	public B require(Fluid fluid, int amount) {
		return require(FluidIngredient.fromFluid(fluid, amount));
	}

	public B require(TagKey<Fluid> fluidTag, int amount) {
		return require(FluidIngredient.fromTag(fluidTag, amount));
	}

	public B require(FluidIngredient ingredient) {
		internal.require(ingredient);
		return self();
	}

	public B output(ItemLike item) {
		return output(item, 1);
	}

	public B output(float chance, ItemLike item) {
		return output(chance, item, 1);
	}

	public B output(ItemLike item, int amount) {
		return output(1, item, amount);
	}

	public B output(float chance, ItemLike item, int amount) {
		return output(chance, new ItemStack(item, amount));
	}

	public B output(ItemStack output) {
		return output(1, output);
	}

	public B output(float chance, ItemStack output) {
		return output(new ProcessingOutput(output, chance));
	}

	public B output(float chance, DatagenMod mod, String id, int amount) {
		return output(new ProcessingOutput(Pair.of(mod.asResource(id), amount), chance));
	}

	public B output(ResourceLocation id) {
		return output(1, id, 1);
	}

	public B output(DatagenMod mod, String id) {
		return output(1, mod.asResource(id), 1);
	}

	public B output(float chance, ResourceLocation registryName, int amount) {
		return output(new ProcessingOutput(Pair.of(registryName, amount), chance));
	}

	public B output(ProcessingOutput output) {
		internal.output(output);
		return self();
	}

	public B output(Fluid fluid, int amount) {
		fluid = FluidHelper.convertToStill(fluid);
		return output(new FluidStack(fluid, amount));
	}

	public B output(FluidStack fluidStack) {
		internal.output(fluidStack);
		return self();
	}

	public B toolNotConsumed() {
		internal.toolNotConsumed();
		return self();
	}

	//

	public B whenModLoaded(String modid) {
		return withCondition(new ModLoadedCondition(modid));
	}

	public B whenModMissing(String modid) {
		return withCondition(new NotCondition(new ModLoadedCondition(modid)));
	}

	public B withCondition(ICondition condition) {
		recipeConditions.add(condition);
		return self();
	}
}
