package com.petrolpark.destroy.content.processing.trypolithography.recipe;

import java.util.stream.Stream;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.petrolpark.destroy.content.processing.trypolithography.CircuitPatternItem;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeSerializer;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CircuitSequencedAssemblyRecipe extends SequencedAssemblyRecipe {

    public CircuitSequencedAssemblyRecipe(ResourceLocation recipeId, SequencedAssemblyRecipeSerializer serializer) {
        super(recipeId, serializer);
    };

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        ItemStack stack = resultPool.get(0).getStack().copy();
        CircuitPatternItem.putPattern(stack, CircuitDeployerApplicationRecipe.EXAMPLE_PATTERN);
        return stack;
    };

    public static class Serializer extends SequencedAssemblyRecipeSerializer {

        @Override
        public SequencedAssemblyRecipe readFromJson(ResourceLocation recipeLoc, JsonObject recipeJson) {
            SequencedAssemblyRecipe recipe = copy(super.readFromJson(recipeLoc, recipeJson));
            
            boolean inputHasPattern = !Stream.of(recipe.getIngredient().getItems()).anyMatch(stack -> !(stack.getItem() instanceof CircuitPatternItem));
            if (!inputHasPattern && recipe.getLoops() != 1) throw new JsonSyntaxException("Circuit Sequenced Assembly recipes may not loop (unless the Circuit Pattern is determined by the input item), as it makes ambigious the step at which the Circuit Pattern of the result is determined.");
            boolean foundConferringRecipe = inputHasPattern;
            for (SequencedRecipe<?> subRecipe : recipe.getSequence()) {
                if (subRecipe.getRecipe() instanceof IConfersCircuitPatternRecipe) {
                    if (foundConferringRecipe) {
                        throw new JsonSyntaxException("Circuit Sequenced Assembly recipes may only define a single sub-recipe that can determine the Circuit Pattern of the result.");
                    } else {
                        foundConferringRecipe = true;
                    };
                };
            };
            if (!foundConferringRecipe) throw new JsonSyntaxException("Circuit Sequenced Assembly recipes must define a step in which the Circuit Pattern of the result gets determined.");
            return recipe;
        };

        @Override
        protected SequencedAssemblyRecipe readFromBuffer(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return copy(super.readFromBuffer(recipeId, buffer));
        };

        public CircuitSequencedAssemblyRecipe copy(SequencedAssemblyRecipe wrappedRecipe) {
            CircuitSequencedAssemblyRecipe recipe = new CircuitSequencedAssemblyRecipe(wrappedRecipe.getId(), this);
            recipe.ingredient = wrappedRecipe.getIngredient();
            recipe.sequence = wrappedRecipe.getSequence();
            recipe.loops = wrappedRecipe.getLoops();
            recipe.transitionalItem = new ProcessingOutput(wrappedRecipe.getTransitionalItem(), 1);
            recipe.resultPool.addAll(wrappedRecipe.resultPool);
            return recipe;
        };
    };
};
