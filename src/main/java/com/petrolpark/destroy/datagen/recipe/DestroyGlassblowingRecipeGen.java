package com.petrolpark.destroy.datagen.recipe;

import com.petrolpark.destroy.DestroyBlocks;
import com.petrolpark.destroy.DestroyFluids;
import com.petrolpark.destroy.DestroyRecipeTypes;
import com.petrolpark.destroy.content.processing.glassblowing.GlassblowingRecipe;
import com.petrolpark.destroy.core.data.recipe.ExtendedProcessingRecipeBuilder;
import com.petrolpark.destroy.core.data.recipe.ExtendedProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class DestroyGlassblowingRecipeGen extends ExtendedProcessingRecipeGen<GlassblowingRecipe, DestroyGlassblowingRecipeGen.Builder> {

    GeneratedRecipe
    BEAKER = create("beaker", b -> b
        .require(DestroyFluids.MOLTEN_BOROSILICATE_GLASS.get(), 250)
        .output(DestroyBlocks.BEAKER)
        .shape(1, 4)
        .shape(7, 3)),
    MEASURING_CYLINDER = create("measuring_cylinder", b -> b
        .require(DestroyFluids.MOLTEN_BOROSILICATE_GLASS.get(), 250)
        .output(DestroyBlocks.MEASURING_CYLINDER)
        .shape(12, 2)
        .shape(2, 3)),
    ROUND_BOTTOMED_FLASK = create("round_bottomed_flask", b -> b
        .require(DestroyFluids.MOLTEN_BOROSILICATE_GLASS.get(), 250)
        .output(DestroyBlocks.ROUND_BOTTOMED_FLASK)
        .shape(1, 2)
        .shape(4, 1)
        .shape(6, 3))
    ;

    public DestroyGlassblowingRecipeGen(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DestroyRecipeTypes.GLASSBLOWING;
    }

    @Override
    protected DestroyGlassblowingRecipeGen.Builder builder(ProcessingRecipeBuilder.ProcessingRecipeFactory<GlassblowingRecipe> factory, ResourceLocation path) {
        return new Builder(factory, path);
    }

    public class Builder extends ExtendedProcessingRecipeBuilder<GlassblowingRecipe, DestroyGlassblowingRecipeGen.Builder> {
        public List<GlassblowingRecipe.BlowingShape> blowingShapes = new ArrayList<>();

        public Builder(ProcessingRecipeBuilder.ProcessingRecipeFactory<GlassblowingRecipe> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        public Builder shape(float length, float radius) {
            blowingShapes.add(new GlassblowingRecipe.BlowingShape(length, radius));
            return this;
        }

        @Override
        public void build(GlassblowingRecipe recipe) {
            recipe.blowingShapes = blowingShapes;
        }
    }
}
