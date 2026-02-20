package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.content.processing.treetap.TappingRecipe;
import com.petrolpark.destroy.core.chemistry.NamedMixtures;
import com.petrolpark.destroy.core.data.recipe.ExtendedProcessingRecipeBuilder;
import com.petrolpark.destroy.core.data.recipe.ExtendedProcessingRecipeGen;
import com.petrolpark.destroy.core.recipe.ingredient.BlockIngredient;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class DestroyTappingRecipeGen extends ExtendedProcessingRecipeGen<TappingRecipe, DestroyTappingRecipeGen.Builder> {

    GeneratedRecipe
    JUNGLE_LOG = create("jungle_log", b -> b
        .requiresBlock(Blocks.STRIPPED_JUNGLE_LOG)
        .requiresBlock(Blocks.STRIPPED_JUNGLE_WOOD)
        .output(NamedMixtures.LATEX.get(10)));

    public DestroyTappingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.TAPPING;
    }

    @Override
    protected Builder builder(ProcessingRecipeBuilder.ProcessingRecipeFactory<TappingRecipe> factory, ResourceLocation path) {
        return new Builder(factory, path);
    }

    public class Builder extends ExtendedProcessingRecipeBuilder<TappingRecipe, Builder> {
        private List<BlockIngredient.Value> blockValues = new ArrayList<>();

        public Builder(ProcessingRecipeBuilder.ProcessingRecipeFactory<TappingRecipe> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        public Builder requiresBlock(Block block) {
            blockValues.add(new BlockIngredient.BlockValue(block));
            return this;
        }

        public Builder requiresBlock(TagKey<Block> tag) {
            blockValues.add(new BlockIngredient.TagValue(tag));
            return this;
        }

        @Override
        public void build(TappingRecipe recipe) {
            recipe.setBlock(BlockIngredient.fromValues(blockValues.stream()));
        }
    }
}
