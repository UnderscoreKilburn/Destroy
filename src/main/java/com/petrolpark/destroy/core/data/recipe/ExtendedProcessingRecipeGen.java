package com.petrolpark.destroy.core.data.recipe;

import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Mostly copied from com.simibubi.create.api.data.recipe.ProcessingRecipeGen.
 * Having to pass both the class of the recipe and the class of the recipe builder feels really redundant and it
 * would be really cool if there was a way to deduce the recipe class just from the builder alone.
 */
public abstract class ExtendedProcessingRecipeGen<T extends ProcessingRecipe<?>, B extends ExtendedProcessingRecipeBuilder<T, B>> extends BaseRecipeProvider {

	public ExtendedProcessingRecipeGen(PackOutput generator, String defaultNamespace) {
		super(generator, defaultNamespace);
	}

	protected abstract B builder(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, ResourceLocation path);

	/**
	 * Create a processing recipe with a single itemstack ingredient, using its id
	 * as the name of the recipe
	 */
	protected GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<B> transform) {
		ProcessingRecipeSerializer<T> serializer = getSerializer();
		GeneratedRecipe generatedRecipe = c -> {
			ItemLike itemLike = singleIngredient.get();
			transform
				.apply(builder(serializer.getFactory(),
					ResourceLocation.fromNamespaceAndPath(namespace, CatnipServices.REGISTRIES.getKeyOrThrow(itemLike.asItem())
						.getPath())).withItemIngredients(Ingredient.of(itemLike)))
				.build(c);
		};
		all.add(generatedRecipe);
		return generatedRecipe;
	}

	/**
	 * Create a new processing recipe, with supplied name and recipe definitions
	 * provided by the function
	 */
	protected GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<B> transform) {
		ProcessingRecipeSerializer<T> serializer = getSerializer();
		GeneratedRecipe generatedRecipe =
			c -> transform.apply(builder(serializer.getFactory(), name.get()))
				.build(c);
		all.add(generatedRecipe);
		return generatedRecipe;
	}

	/**
	 * Create a new processing recipe, with recipe definitions provided by the
	 * function
	 */
	protected GeneratedRecipe create(ResourceLocation name, UnaryOperator<B> transform) {
		return createWithDeferredId(() -> name, transform);
	}

	/**
	 * Gets this recipe generators generated recipe type.
	 * Subclasses should override this to return an instance of IRecipeTypeInfo
	 * Create uses an enum, however this is not in any way required for addons.
	 */
	protected abstract IRecipeTypeInfo getRecipeType();

	protected ProcessingRecipeSerializer<T> getSerializer() {
		return getRecipeType().getSerializer();
	}

	protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
		return () -> {
			ResourceLocation registryName = CatnipServices.REGISTRIES.getKeyOrThrow(item.get()
					.asItem());
			return asResource(registryName.getPath() + suffix);
		};
	}

	/**
	 * Create a new processing recipe, with recipe definitions provided by the
	 * function, under the default namespace
	 */
	protected GeneratedRecipe create(String name, UnaryOperator<B> transform) {
		return create(asResource(name), transform);
	}

	/**
	 * Create a processing recipe with a single itemstack ingredient, using its id
	 * as the name of the recipe, under the default namespace
	 */
	protected GeneratedRecipe create(Supplier<ItemLike> singleIngredient, UnaryOperator<B> transform) {
		return create(modid, singleIngredient, transform);
	}


	/**
	 * Gets a display name for this recipe generator.
	 * It is recommended to override this for a prettier name, however that is not
	 * required.
	 */
	@NotNull
	@Override
	public String getName() {
		return modid + "'s processing recipes: " + getRecipeType().getId()
			.getPath();
	}

}
