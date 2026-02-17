package com.petrolpark.destroy.core.data.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class DestroyRecipeProvider extends RecipeProvider {
    static final List<ProcessingRecipeGen> GENERATORS = new ArrayList<>();

    public DestroyRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {}

    public static void registerAllProcessing(DataGenerator gen, PackOutput output) {
        // TODO: Add generators for Destroy's processing recipes here

    }
}
