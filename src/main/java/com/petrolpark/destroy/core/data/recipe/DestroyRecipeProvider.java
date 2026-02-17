package com.petrolpark.destroy.core.data.recipe;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.content.processing.treetap.DestroyTappingRecipeGen;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class DestroyRecipeProvider extends RecipeProvider {
    static final List<ProcessingRecipeGen> GENERATORS = new ArrayList<>();

    public DestroyRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {}

    public static void registerAllProcessing(DataGenerator gen, PackOutput output) {
        // Add generators for Destroy's processing recipes here
        GENERATORS.add(new DestroyTappingRecipeGen(output, Destroy.MOD_ID));

        gen.addProvider(true, new DataProvider() {
            @Override
            public String getName() {
                return "Destroy's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                    .map(gen -> gen.run(dc))
                    .toArray(CompletableFuture[]::new));
            }
        });
    }
}
