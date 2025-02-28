package com.petrolpark.destroy.content.processing.phytomining;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyItems;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class PhytominingRecipe extends ProcessingRecipe<RecipeWrapper> {

    private static int counter = 0;

    private CropMutation mutation;

    public static PhytominingRecipe create(CropMutation mutation) {
        ProcessingRecipeBuilder<PhytominingRecipe> recipeBuilder = new ProcessingRecipeBuilder<>(PhytominingRecipe::new, Destroy.asResource("mutation_" + counter++))
            .withItemIngredients(Ingredient.of(new ItemStack(mutation.getStartCropSupplier().get().asItem(), 1)), Ingredient.of(new ItemStack(DestroyItems.HYPERACCUMULATING_FERTILIZER.get(), 1)))
            .withItemOutputs(new ProcessingOutput(new ItemStack(mutation.getResultantCropSupplier().get().getBlock().asItem(), 1), 1.0f));
        if (mutation.isOreSpecific()) {
            Block oreBlock = mutation.getOreSupplier().get();
            recipeBuilder
                .withItemIngredients(Ingredient.of(new ItemStack(oreBlock.asItem(), 1)))
                .withItemOutputs(new ProcessingOutput(new ItemStack(mutation.getResultantBlockUnder(oreBlock.defaultBlockState()).getBlock().asItem(), 1), 1.0f));
        };
        PhytominingRecipe recipe = recipeBuilder.build();
        recipe.mutation = mutation;
        return recipe;
    };

    public PhytominingRecipe(ProcessingRecipeParams params) {
        super(DestroyRecipeTypes.MUTATION, params);
    };

    public CropMutation getMutation() {
        return this.mutation;
    }

    @Override
    public boolean matches(RecipeWrapper container, Level level) {
        return false;
    };

    @Override
    protected int getMaxInputCount() {
        return 2;
    };

    @Override
    protected int getMaxOutputCount() {
        return 2;
    };
};
