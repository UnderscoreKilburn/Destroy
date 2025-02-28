package com.petrolpark.destroy.compat.jei.recipemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.compat.jei.category.GenericReactionCategory;
import com.petrolpark.destroy.compat.jei.category.ReactionCategory;
import com.petrolpark.destroy.core.chemistry.recipe.ReactionRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;

/**
 * If a reversible Reaction has an Item Stack ingredient, then by default JEI will not show that reversible Reaction as a possible recipe for that precipitate.
 * This recipe manager plugin remedies that.
 */
public class ItemReverseReactionRecipeManagerPlugin implements IRecipeManagerPlugin {

    public static final List<RecipeType<?>> TYPES = List.of(ReactionCategory.TYPE, GenericReactionCategory.TYPE);

    @Override
    public <V> List<RecipeType<?>> getRecipeTypes(IFocus<V> focus) {
        return TYPES;
    };

    @Override
    @SuppressWarnings("unchecked")
    public <T, V> List<T> getRecipes(IRecipeCategory<T> recipeCategory, IFocus<V> focus) {
        List<T> recipes = new ArrayList<>();
        focus.checkedCast(VanillaTypes.ITEM_STACK).map(IFocus::getTypedValue).map(ITypedIngredient::getIngredient).ifPresent(stack -> {
            Stream<? extends ReactionRecipe> recipesToCheck;
            if (recipeCategory instanceof GenericReactionCategory) recipesToCheck = GenericReactionCategory.RECIPES.values().stream();
            else if (recipeCategory instanceof ReactionCategory) recipesToCheck = ReactionCategory.RECIPES.values().stream();
            else return;
            recipesToCheck.filter(recipe -> {
                LegacyReaction reaction = recipe.getReaction();

                boolean
                searchCatalysts = focus.getRole() == RecipeIngredientRole.CATALYST,
                searchInputs = searchCatalysts || focus.getRole() == RecipeIngredientRole.INPUT || (focus.getRole() == RecipeIngredientRole.OUTPUT && reaction.displayAsReversible()),
                searchOutputs = searchCatalysts || focus.getRole() == RecipeIngredientRole.OUTPUT || (focus.getRole() == RecipeIngredientRole.INPUT && reaction.displayAsReversible());
                
                // Check for reactants and catalysts
                if (reaction.getItemReactants().stream().anyMatch(ir -> ((ir.isCatalyst() && searchCatalysts) || (!ir.isCatalyst() && searchInputs)) && ir.isItemValid(stack))) return true;
                
                // Check for precipitates
                if (searchOutputs && reaction.hasResult()) reaction.getResult().getAllPrecipitates().stream().anyMatch(p -> ItemStack.matches(p.getPrecipitate(), stack));

                return false;
            // Unchecked conversion (but actually it is checked)
            }).map(r -> (T)r).forEach(recipes::add);

        });
        return recipes;
    };

    @Override
    public <T> List<T> getRecipes(IRecipeCategory<T> recipeCategory) {
        return List.of();
    };
    
};
