package com.petrolpark.destroy.compat.tfmg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.petrolpark.compat.CompatMods;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.content.processing.distillation.DistillationRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.RecipeFinder;

import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLLoader;

public class SharedDistillationRecipes {

    public static final String TFMGfractionalDistillationName = "tfmg:advanced_distillation";
    public static final String TFMGDistillationName = "tfmg:distillation";
    private static final Object TFMGRecipeCacheKey = new Object();
    private static final Object DestroyRecipeCacheKey = new Object();

    private static final Map<Level, List<DistillationRecipe>> TFMG_TO_DESTROY_RECIPES = new HashMap<>();
    private static final Map<Level, List<ProcessingRecipe<?>>> DESTROY_TO_TFMG_RECIPES = new HashMap<>();

    public static final List<DistillationRecipe> getTFMGToDestroyRecipes(Level level) {
        if (!CompatMods.TFMG.isLoaded() || !DestroyAllConfigs.SERVER.compat.TFMGDistillationInDestroy.get()) return Collections.emptyList();
        return TFMG_TO_DESTROY_RECIPES.computeIfAbsent(level, SharedDistillationRecipes::convertTFMGToDestroyRecipes);
    };

    public static final List<ProcessingRecipe<?>> getDestroyToTFMGRecipes(Level level) {
        if (!CompatMods.TFMG.isLoaded() || !DestroyAllConfigs.SERVER.compat.destroyDistillationInTFMG.get()) return Collections.emptyList();
        return DESTROY_TO_TFMG_RECIPES.computeIfAbsent(level, SharedDistillationRecipes::convertDestroyToTFMGRecipes);
    };
    
    private static List<DistillationRecipe> convertTFMGToDestroyRecipes(Level level) {
        List<DistillationRecipe> recipes = new ArrayList<>();
        if (FMLLoader.getLoadingModList().getModFileById(CompatMods.TFMG.id) == null) return recipes;

        for (ProcessingRecipe<?> recipe : 
            RecipeFinder.get(TFMGRecipeCacheKey, level, r -> 
                r instanceof ProcessingRecipe<?> processingRecipe
                && (r.getType().toString().equals(TFMGfractionalDistillationName) || r.getType().toString().equals(TFMGDistillationName))
                && (processingRecipe.getRollableResults().stream().allMatch(output -> output.getStack().isEmpty()))
            ).stream().map(r -> (ProcessingRecipe<?>)r).toList()
        ) {
            recipes.add(new ProcessingRecipeBuilder<>(DistillationRecipe::new, Destroy.asResource("tfmg_compat_" + recipe.getId().getPath()))
                .withFluidIngredients(recipe.getFluidIngredients())
                .withFluidOutputs(recipe.getFluidResults())
                .requiresHeat(HeatCondition.HEATED)
                .build()
            );
        };
        return recipes;
    };

    private static List<ProcessingRecipe<?>> convertDestroyToTFMGRecipes(Level level) {
        List<ProcessingRecipe<?>> recipes = new ArrayList<>();
        RecipeFinder.get(DestroyRecipeCacheKey, level, r -> 
            r instanceof DistillationRecipe recipe
            && r.getType() == DestroyRecipeTypes.DISTILLATION.getType()
            && recipe.getFluidResults().size() <= 6
            && recipe.getRequiredHeat().testBlazeBurner(HeatLevel.KINDLED)
        ).stream().map(r -> (ProcessingRecipe<?>)r).forEach(recipes::add);
        return recipes;
    };
};
